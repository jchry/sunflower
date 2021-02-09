package com.jpeony.sunflower.agent.processor;

import com.jpeony.sunflower.common.protocol.RequestCode;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public class ErrorMonitorProcessor extends AbstractMonitorProcessor {
    public void errorMonitor() {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_ERROR_MONITOR_MESSAGE);
        request.setRemark("message");
        send(request);
    }
}
