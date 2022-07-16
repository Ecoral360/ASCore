package org.ascore.executor;

import org.ascore.errors.ASCErrors;

public sealed interface ASCExecutionResult permits
        ASCExecutionResult.StopSendData,
        ASCExecutionResult.Success,
        ASCExecutionResult.Value {

    record StopSendData(String dataString) implements ASCExecutionResult {
        public void send() {
            throw new ASCErrors.StopSendData(dataString);
        }
    }

    record Success(String dataString) implements ASCExecutionResult {
    }

    record Value(String value) implements ASCExecutionResult {
    }
}
