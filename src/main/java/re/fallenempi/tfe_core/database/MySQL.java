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

/*
 * 
 * Name: MySQL.java
 * Version: 0.2
 * Date: 2017-09-25
 * Author: Kevin Olinger <https://kevyn.lu>
 * 
 * This class is being used in some of our Java projects. Currently in:
 *  - tfempire/The Fallen Empire Core (Realtime information processor)
 *  - tfempire/The Fallen Empire (Spigot & Sponge Plugin)
 * 
 */

package re.fallenempi.tfe_core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import re.fallenempi.tfe_core.Core;

public class MySQL {

	Core TFE;
	HikariDataSource DS = null;
	
	public Insert insert;
	public Select get;
	public Update update;
	public Delete delete;
	
	public MySQL(Core TFE) {
		this.TFE = TFE;
		
		insert = new Insert(TFE, this);
		get = new Select(TFE, this);
		update = new Update(TFE, this);
		delete = new Delete(TFE, this);
	}
	
	public void connect() {
		try {
			HikariConfig hConfig = new HikariConfig();
			
			hConfig.setMaximumPoolSize(10);
			hConfig.setMinimumIdle(0);
			hConfig.setIdleTimeout(360000);
			hConfig.setMaxLifetime(400000);
			hConfig.setDataSourceClassName("org.mariadb.jdbc.MySQLDataSource");
			hConfig.addDataSourceProperty("portNumber", TFE.config.getInt("MySQL > port"));
			hConfig.addDataSourceProperty("serverName", TFE.config.get("MySQL > host"));
			hConfig.addDataSourceProperty("databaseName", TFE.config.get("MySQL > database"));
			hConfig.addDataSourceProperty("user", TFE.config.get("MySQL > user"));
			hConfig.addDataSourceProperty("password", TFE.config.get("MySQL > password"));
			
			DS = new HikariDataSource(hConfig);
			DS.setConnectionTimeout(15000);
			
			TFE.MYSQL = true;
			//Log: Successfully established a connection
			TFE.log.info("Connected to db");
		} catch(Exception ex) {
			TFE.MYSQL = false;
			//Log: Could not connect
			TFE.log.error("Could not connect to db");
			ex.printStackTrace();
		}
	}
	
	public void close() {
		TFE.MYSQL = false;
		
		DS.close();
	}
	
	public void close(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch(SQLException ex) {
				//Log: Could not close connection
			}
		}
	}
	
	public void close(PreparedStatement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException ex) {
				//Log: Could not close prepared statement
			}
		}
	}
	
	public void close(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException ex) {
				//Log: Could not close statement
			}
		}
	}
	
	public void close(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch(SQLException ex) {
				//Log: Could not close result set
			}
		}
	}
	
}
