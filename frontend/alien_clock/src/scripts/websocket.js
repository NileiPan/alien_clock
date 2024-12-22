// src/services/websocket.js

import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const SOCKET_URL = 'http://localhost:8080/ws';

class WebSocketService {
    constructor() {
        this.client = null;
    }

    connect() {
        return new Promise((resolve, reject) => {
            this.client = new Client({
                brokerURL: null, // Use SockJS instead of native WebSocket
                webSocketFactory: () => new SockJS(SOCKET_URL),
                debug: (str) => console.log(str),
                reconnectDelay: 5000,
                onConnect: () => resolve(),
                onStompError: (frame) => {
                    console.error('STOMP error', frame);
                    reject(frame);
                },
            });
            this.client.activate();
        });
    }

    subscribe(destination, callback) {
        if (this.client && this.client.connected) {
            this.client.subscribe(destination, (message) => {
                callback(JSON.parse(message.body));
            });
        }
    }

    send(destination, payload) {
        if (this.client && this.client.connected) {
            this.client.publish({ destination, body: JSON.stringify(payload) });
        }
    }

    disconnect() {
        if (this.client) {
            this.client.deactivate();
        }
    }
}

export default new WebSocketService();
