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
 * Name: JsonFile.java
 * Version: 0.2
 * Date: 2017-09-25
 * Author: Kevin Olinger <https://kevyn.lu>
 * 
 * This class is being used in some of our Java projects. Currently in:
 *  - tfempire/The Fallen Empire Core (Realtime information processor)
 *  - tfempire/The Fallen Empire (Spigot & Sponge Plugin)
 *  - tfempire/The Fallen Empire Bungee (BungeeCord Plugin)
 *  
 *  Todo:
 *   - Replace log comments with calls to proper logging methods
 *   - Support for multidimensional JSON files
 *   - Comments/Description of the methods
 *   - Check if keys are valid (no spaces, seperated with dots, etc ..)
 * 
 */

package re.fallenempi.tfe_core.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import re.fallenempi.tfe_core.Core;
import re.fallenempi.tfe_core.Info;

public class JsonFile {
	
	Core TFE;
	File jsonFile;
	
	ObjectMapper mapper = new ObjectMapper();
	ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
	
	Map<String, String> fileContent = new TreeMap<String, String>();
	HashMap<String, String> jsonFileContent = new HashMap<String, String>();
	
	boolean ERROR = false;
	boolean UPDATE = false;
	
	public JsonFile(Core TFE, String file) {
		this.TFE = TFE;
		
		jsonFile = new File(System.getProperty("user.dir") +"/config/"+ file +".json");
		
		initFile();
		checkValues();
		if(!ERROR) updateFile();
	}
	
	private void initFile() {
		File dataFolder = jsonFile.getParentFile();
		
		if(!dataFolder.exists() && !dataFolder.mkdirs()) {
			//Log error
			
			ERROR = true;
		}
		
		if(!jsonFile.exists() && !ERROR) {			
			try {
				ObjectNode obj = mapper.createObjectNode();
				
				obj.put("application", Info.SHORT);
				obj.put("version", Info.VERSION);
				
				writer.writeValue(jsonFile, obj);
				
				//Log: Creating file ..
			} catch (IOException ex) {
				ex.printStackTrace();
				
				//Log: Unable to create .. using default values
				
				ERROR = true;
			}
		} else if(!jsonFile.exists() && !ERROR) {
			//Log
			//File found
		}
		
		if(!ERROR) {
			try {
				Iterator<Entry<String, JsonNode>> jNodes = mapper.readTree(jsonFile).fields();
				
				while(jNodes.hasNext()) {
					Map.Entry<String, JsonNode> jNode = (Map.Entry<String, JsonNode>) jNodes.next();

					jsonFileContent.put(jNode.getKey(), jNode.getValue().asText());					
				}
			} catch(IOException ex) {
				ex.printStackTrace();
				
				//Log: Unable to read .. using default values
				
				ERROR = true;
			}
		}
	}
	
	private void updateFile() {
		if(UPDATE) {
			try {
				ObjectNode obj = mapper.createObjectNode();
				
				for(Map.Entry<String, String> entry: fileContent.entrySet()) obj.put(entry.getKey(), entry.getValue());
				
				writer.writeValue(jsonFile, obj);
			} catch(IOException ex) {
				//Log: Unable to update
			}
		}
	}
	
	public void checkValue(String key, String defaultValue) {
		if(ERROR) fileContent.put(key, defaultValue);
		else {
			if(!jsonFileContent.containsKey(key)) {
				jsonFileContent.put(key, defaultValue);
				fileContent.put(key, defaultValue);
				
				UPDATE = true;
			} else fileContent.put(key, jsonFileContent.get(key));
		}
	}
	
	public void checkValues() {
		checkValue("application", Info.SHORT);
		checkValue("version", Info.VERSION);
	}
	
	//Get methods
	public String get(String key) {
		if(fileContent.containsKey(key)) return fileContent.get(key);
		else {
			//Log: Undefined key requested
			return "{Undefined key}";		
		}
	}
	
	public Integer getInt(String key) {
		if(fileContent.containsKey(key)) return Integer.parseInt(get(key));
		else {
			//Log: Undefined key requested
			return 0;		
		}
	}
	
	public Boolean getBool(String key) {
		if(fileContent.containsKey(key)) {
			String value = get(key).toUpperCase();
			
			if(value.equals("TRUE")) return true;
			else return false;
		} else //Log: Undefined key requested
			
		return false;
	}

}
