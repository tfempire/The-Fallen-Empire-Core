/**
 * 
 * BSD 3-Clause License
 * 
 * Copyright (c) 2017+ Fallen Studios, The Fallen Empire & Kevin Olinger
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *    
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *    
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package re.fallenempi.tfe_core;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;

import com.fasterxml.jackson.databind.JsonNode;

import re.fallenempi.tfe_core.event.AuthenticateEvent;
import re.fallenempi.tfe_core.event.ChatEvent;
import re.fallenempi.tfe_core.event.ConnectEvent;
import re.fallenempi.tfe_core.event.DisconnectEvent;

public class Server implements Runnable {

	Core TFE;
	
	static Thread thread;
	private SocketIOServer server = null;
	
	private String host = "localhost";
	private Integer port = 8080;
	
	public Server(Core TFE) {
		this.TFE = TFE;
		
		host = TFE.config.get("Socket server > host");
		port = TFE.config.getInt("Socket server > port");
	}
	
	public void start() {
		if(thread != null) return;

		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		TFE.log.info("Stopping Socket.IO server ..");
		TFE.log.socket.info("Stopping Socket.IO server ..");
		
		server.stop();
		thread.interrupt();
		
		TFE.log.socket.info("Stopped Socket.IO server.");
		
		thread = null;
	}

	@Override
	public void run() {
		SocketConfig sconfig = new SocketConfig();
		sconfig.setReuseAddress(true);
		
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);
		config.setSocketConfig(sconfig);
		
		server = new SocketIOServer(config);
		
		server.addConnectListener(new ConnectEvent(TFE));
		server.addDisconnectListener(new DisconnectEvent(TFE));
		
		server.addEventListener("authenticate", JsonNode.class, new AuthenticateEvent(TFE));
		server.addEventListener("chat", JsonNode.class, new ChatEvent(TFE));
		
		TFE.log.info("Starting Socket.IO server on "+ host +":"+ port +" ..");
		TFE.log.socket.info("Starting Socket.IO server on "+ host +":"+ port +" ..");
		
        server.start();	
        
        TFE.log.socket.info("Started Socket.IO server.");
        TFE.log.info("The Fallen Empire Core is now fully started.");
        TFE.log.emptyLine();
	}
	
	public SocketIOServer get() {
		return server;
	}
	
}
