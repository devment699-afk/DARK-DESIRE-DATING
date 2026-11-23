const WebSocket = require('ws');

const PORT = process.env.PORT || 8080;
const wss = new WebSocket.Server({ port: PORT });

console.log(`[DARK DESIRE SERVER] WebSocket Server ignited on port ${PORT}`);

// Clients: Map of userId -> { ws, profile, partnerId }
const clients = new Map();
// Queue: Array of userIds waiting to be matched
const waitingQueue = [];

wss.on('connection', (ws) => {
    let currentUserId = null;

    ws.on('message', (messageRaw) => {
        try {
            const data = JSON.parse(messageRaw);
            const { action } = data;

            switch (action) {
                case 'join_queue': {
                    currentUserId = data.userId;
                    clients.set(currentUserId, {
                        ws,
                        profile: {
                            userId: data.userId,
                            alias: data.alias || 'Secret Stranger',
                            age: data.age || 22,
                            gender: data.gender || 'Other',
                            lookingFor: data.lookingFor || 'Anyone',
                            bio: data.bio || '',
                            avatarIndex: 0
                        },
                        partnerId: null
                    });

                    // Remove from queue if already in queue
                    const existingIdx = waitingQueue.indexOf(currentUserId);
                    if (existingIdx !== -1) waitingQueue.splice(existingIdx, 1);

                    // Check if anyone is waiting
                    matchUsers(currentUserId);
                    break;
                }

                case 'leave_queue': {
                    const idx = waitingQueue.indexOf(data.userId);
                    if (idx !== -1) waitingQueue.splice(idx, 1);
                    break;
                }

                case 'send_message': {
                    const client = clients.get(data.partnerId);
                    if (client && client.ws.readyState === WebSocket.OPEN) {
                        client.ws.send(JSON.stringify({
                            action: 'chat_message',
                            message: {
                                id: 'msg_' + Date.now(),
                                senderId: currentUserId,
                                text: data.text,
                                timestamp: data.timestamp || Date.now(),
                                isSelf: false
                            }
                        }));
                    }
                    break;
                }

                case 'typing': {
                    const client = clients.get(data.partnerId);
                    if (client && client.ws.readyState === WebSocket.OPEN) {
                        client.ws.send(JSON.stringify({
                            action: 'partner_typing',
                            isTyping: data.isTyping
                        }));
                    }
                    break;
                }

                case 'leave_chat': {
                    if (data.partnerId) {
                        const partner = clients.get(data.partnerId);
                        if (partner && partner.ws.readyState === WebSocket.OPEN) {
                            partner.ws.send(JSON.stringify({ action: 'partner_disconnected' }));
                            partner.partnerId = null;
                        }
                    }
                    if (currentUserId && clients.has(currentUserId)) {
                        clients.get(currentUserId).partnerId = null;
                    }
                    break;
                }
            }
        } catch (err) {
            console.error('Error handling message:', err.message);
        }
    });

    ws.on('close', () => {
        if (currentUserId) {
            const idx = waitingQueue.indexOf(currentUserId);
            if (idx !== -1) waitingQueue.splice(idx, 1);

            const user = clients.get(currentUserId);
            if (user && user.partnerId) {
                const partner = clients.get(user.partnerId);
                if (partner && partner.ws.readyState === WebSocket.OPEN) {
                    partner.ws.send(JSON.stringify({ action: 'partner_disconnected' }));
                    partner.partnerId = null;
                }
            }
            clients.delete(currentUserId);
        }
    });
});

function matchUsers(userId) {
    const user = clients.get(userId);
    if (!user) return;

    if (waitingQueue.length > 0) {
        // Match with the first waiting stranger
        const partnerId = waitingQueue.shift();
        const partner = clients.get(partnerId);

        if (partner && partner.ws.readyState === WebSocket.OPEN) {
            user.partnerId = partnerId;
            partner.partnerId = userId;

            // Notify User
            user.ws.send(JSON.stringify({
                action: 'match_found',
                partner: partner.profile
            }));

            // Notify Partner
            partner.ws.send(JSON.stringify({
                action: 'match_found',
                partner: user.profile
            }));
            console.log(`[MATCH] ${user.profile.alias} matched with ${partner.profile.alias}`);
            return;
        }
    }

    // No immediate match found, place into queue
    waitingQueue.push(userId);
    console.log(`[QUEUE] User ${user.profile.alias} waiting in queue. Queue size: ${waitingQueue.length}`);
}
