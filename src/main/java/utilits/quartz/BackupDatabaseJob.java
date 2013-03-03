package utilits.quartz;

import org.apache.commons.io.FileUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Here will be javadoc
 *
 * @author karlovsky
 * @since 1.0
 */
public class BackupDatabaseJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(BackupDatabaseJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Database backup started...");
        try {
            long startTime = System.currentTimeMillis();
            JobDataMap params = context.getMergedJobDataMap();
            MessageChannel ftpChannel = (MessageChannel) params.get("ftpChannel");
            File tmpDatabaseDir = makeTmpDatabaseDir();
            File backup = makeCompressedDatabaseFile(tmpDatabaseDir);
            Message<File> message = MessageBuilder.withPayload(backup).build();
            ftpChannel.send(message);
            long endTime = System.currentTimeMillis() - startTime;
            logger.info("Database backup ended, execution time {} ms.", endTime);
            FileUtils.forceDelete(tmpDatabaseDir);
            FileUtils.forceDelete(backup);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private File makeTmpDatabaseDir() throws IOException {
        File databaseDir = new File("database");
        File tmpBackupDir = new File("tmp_backup");
        FileUtils.forceMkdir(tmpBackupDir);
        File tmpDatabaseDir = new File(tmpBackupDir, "database");
        FileUtils.forceMkdir(tmpDatabaseDir);
        FileUtils.copyDirectory(databaseDir, tmpDatabaseDir);
        return tmpDatabaseDir;
    }

    private File makeCompressedDatabaseFile(File tmpDatabaseDir) throws IOException {
        File backup = new File("backup" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(backup));
        compressDirectory(tmpDatabaseDir, out);
        out.close();
        return backup;
    }


    private void compressDirectory(File directory, ZipOutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    compressDirectory(file, out);
                    continue;
                }
                FileInputStream in = new FileInputStream(file);
                String fileName = directory.getName() + File.separator + file.getName();
                logger.info("Adding {} to backup archive.", fileName);
                out.putNextEntry(new ZipEntry(fileName));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }
}
