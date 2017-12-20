package org.smileframework.tool.ftp;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.tool.clazz.ClassTools;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @Package: org.smileframework.tool.ftp
 * @Description: 连接sftp
 * api:https://www.cnblogs.com/longyg/archive/2012/06/25/2556576.html
 * @author: liuxin
 * @date: 2017/12/13 下午12:24
 */
public class SftpClient {
    private static final Logger LOG = LoggerFactory.getLogger(ClassTools.class);


    private String userName;
    private String password;
    private String host;
    private int port;
    private  Session sshSession = null;

    private SftpClient(String userName, String password, String host, Integer port){
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    private ChannelSftp getChannelSftp(){
        ChannelSftp sftp = null;
        Channel channel = null;
        JSch jsch = new JSch();
        try {
            jsch.getSession(userName, host, port);
            sshSession = jsch.getSession(userName, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            LOG.debug("Session connected!");
            channel = sshSession.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        LOG.debug("Channel connected!");
        sftp = (ChannelSftp) channel;
        return sftp;

    }


    private  List<String> listFileNames(String dir) {
        List<String> list = new ArrayList<>();
        ChannelSftp sftp = null;
        try {
            sftp=getChannelSftp();
            Vector<?> vector = sftp.ls(dir);
            for (Object item:vector) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) item;
                System.out.println(entry.getFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeChannel(sftp);
            closeSession(sshSession);
        }
        return list;
    }
    public void put(InputStream src, String dst){
        ChannelSftp channelSftp = getChannelSftp();
        try {
            channelSftp.put(src,dst);
        } catch (SftpException e) {
            e.printStackTrace();
        }finally {
            closeChannel(channelSftp);
            closeSession(sshSession);
        }


    }

    private static void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private static void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public static void main(String[] args)throws Exception {
        SftpClient sftpClient=new SftpClient("srcb", "TTl67GG4V", "139.198.1.43", 22015);
//        sftpClient.listFileNames("/share");
//
//        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("logback.xml");
//        sftpClient.put(resourceAsStream,"/share/test2.text");
    }
}
