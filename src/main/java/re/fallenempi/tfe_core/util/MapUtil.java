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
 * Name: MapUtil.java
 * Version: 0.1
 * Date: 2017-09-26
 * Author: Kevin Olinger <https://kevyn.lu>
 * 
 * This class is being used in some of our Java projects. Currently in:
 *  - tfempire/The Fallen Empire Core (Realtime information processor)
 *  - tfempire/The Fallen Empire (Spigot & Sponge Plugin)
 *  - tfempire/The Fallen Empire Bungee (BungeeCord Plugin)
 * 
 */

package re.fallenempi.tfe_core.util;

import java.util.HashMap;
import java.util.UUID;

import re.fallenempi.tfe_core.Core;

public abstract class MapUtil {

	protected Core TFE;
	
	HashMap<String, HashMap<String, Object>> map = new HashMap<String, HashMap<String, Object>>();
	
	/* CHECK AND CREATE KEYS */
	/* Create a new key */
	private void save(String key) {
		if(!isKeyAvailable(key)) map.put(key, new HashMap<String, Object>());
	}
	
	/*  Check if key exists */
	public boolean isKeyAvailable(String key) {
		if(map.containsKey(key)) return true;
		
		return false;
	}
	
	/* Check if the second key exists */
	private boolean isSndKeyAvailable(String key, String keySnd) {
		if(isKeyAvailable(key)) {
			if(map.get(key).containsKey(keySnd)) return true;
		}
		
		return false;
	}
	
	/* SAVE */
	/* Save - with a string as key */
	public void save(String key, String keySnd, Object value) {
		save(key);
		map.get(key).put(keySnd, value);
	}
	
	/* Save - with a UUID as key */
	public void save(UUID key, String keySnd, Object value) {
		save(key.toString(), keySnd, value);
	}
	
	/* GET */
	/* Get Object - with a string as key */
	public Object get(String key, String keySnd) {
		if(isSndKeyAvailable(key, keySnd)) return map.get(key).get(keySnd);
		
		return "";
	}
	
	/* Get Object - with a UUID as key */
	public Object get(UUID key, String keySnd) {
		return get(key.toString(), keySnd);
	}
	
	/* Get String - with a string as key */
	public String getString(String key, String keySnd) {		
		try { if(isSndKeyAvailable(key, keySnd)) return map.get(key).get(keySnd).toString(); } 
		catch(Exception ex) { TFE.log.error("MapUtil.java > getString() > Tried to cast an object to a string failed. Given keys: "+ key + " & " + keySnd); }
		
		return "";
	}
	
	/* Get String - with a UUID as key */
	public String getString(UUID key, String keySnd) {
		return getString(key.toString(), keySnd);
	}
	
	/* Get Integer - with a string as key */
	public Integer getInt(String key, String keySnd) {
		String result = getString(key, keySnd).toString();
		
		try { if(result != "") return Integer.valueOf(result); } 
		catch(Exception ex) { TFE.log.error("MapUtil.java > getInt() > Tried to cast an object/a string to an integer failed. Given keys: "+ key + " & " + keySnd); }
		
		return 0;
	}
	
	/* Get Integer - with a UUID as key */
	public Integer getInt(UUID key, String keySnd) {
		return getInt(key.toString(), keySnd);
	}
	
}
