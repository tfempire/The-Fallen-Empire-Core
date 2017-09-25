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

import re.fallenempi.tfe_core.config.Config;
import re.fallenempi.tfe_core.database.MySQL;
import re.fallenempi.tfe_core.input.InputReader;
import re.fallenempi.tfe_core.util.LogUtil;

public class Core {
	
	public Boolean DEBUG = false;
	public Boolean MYSQL = false;
	
	public LogUtil log;
	public Config config;
	public MySQL DB;
	public InputReader IR;
	public Server server;
	
	public static void main(String[] args) {
		new Core();
	}
	
	public Core() {
		log = new LogUtil();
		config = new Config(this);
		DB = new MySQL(this);
		
		DEBUG = config.getBool("Debug");
		
		IR = new InputReader(this);
		server = new Server(this);
		
		initMySQL();
		
		IR.start();
		server.start();
	}
	
	public void quit() {
		IR.stop();
		server.stop();
		
		System.exit(0);
	}
	
	//Manage MySQL connection
	public void initMySQL() {
		if(MYSQL) {
			//Log: Could not init MySQL connection. Already established. Type "mysql restart" to restart.
			
			return;
		}
		
		try {
			//Log: Establishing connection
			
			DB.connect();
		} catch(Exception ex) {
			//Log: Could not connect;
			
			MYSQL = false;
		}
	}
	
	public void closeMySQL() {
		if(!MYSQL) {
			//Log: Could not close connection, no connection established.
			
			return;
		}
		
		DB.close();
	}

}
