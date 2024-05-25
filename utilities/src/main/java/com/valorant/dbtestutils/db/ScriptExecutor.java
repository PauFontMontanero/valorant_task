package com.valorant.dbtestutils.db;

import java.io.IOException;
import java.nio.file.Path;

import com.valorant.dbtestutils.exceptions.ScriptExecutorException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

public class ScriptExecutor {
    public static void execute(Path scriptPath) {
        ScriptRunner runner = new ScriptRunner(DbUtils.connectToServer());
        runner.setAutoCommit(true);
        runner.setStopOnError(true);
        try {
            runner.runScript(Resources.getResourceAsReader(scriptPath.toString()));
        } catch (IOException e) {
            throw new ScriptExecutorException(e);
        }
    }
}
