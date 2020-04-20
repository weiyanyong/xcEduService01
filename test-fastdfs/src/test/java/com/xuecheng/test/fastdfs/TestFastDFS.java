package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    @Test
    public void  testUpload(){
        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1  = new StorageClient1(trackerServer,storageServer);
            String filePath = "E:/2.jpg";
            String fileId = storageClient1.upload_file1(filePath, "jpg", null);
           //group1/M00/00/00/wKi8kl6BU6aAfoSSAAJ36kemZsc438.jpg
            System.out.println(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void download(){

        try {
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1  = new StorageClient1(trackerServer,storageServer);

            String fileId = "group1/M00/00/00/wKi8kl6BU6aAfoSSAAJ36kemZsc438.jpg";
            byte[] bytes = storageClient1.download_file1(fileId);
            FileOutputStream fileOutputStream = new FileOutputStream( new File("e:/haha.jpg"));
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


}
