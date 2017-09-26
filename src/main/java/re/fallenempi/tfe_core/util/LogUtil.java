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

package re.fallenempi.tfe_core.util;

import re.fallenempi.tfe_core.Core;
import re.fallenempi.tfe_core.util.log.ChatLog;
import re.fallenempi.tfe_core.util.log.CoreLog;
import re.fallenempi.tfe_core.util.log.SocketLog;

public class LogUtil {

	Core TFE;
	
	public CoreLog core;
	public ChatLog chat;
	public SocketLog socket;
	
	public LogUtil() {
		core = new CoreLog(TFE);
		chat = new ChatLog(TFE);
		socket = new SocketLog(TFE);
	}
	
	public void emptyLine() {
		System.out.println("");
	}
	
	public void info(String message) { core.info(message); }
	public void warn(String message) { core.warn(message); }
	public void error(String message) { core.error(message); }
	public void debug(String message) { core.debug(message); }
	public void fatal(String message) { core.fatal(message); }
	
}
