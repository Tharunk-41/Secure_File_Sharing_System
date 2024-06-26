package service;

import model.CustomMultipartFile;
import model.myFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Service
public class DBService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addFile(MultipartFile upload) throws IOException {

        // define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        // store in database which returns the objectID
        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                metadata);

        // return as a string
        System.out.println(fileID.toString());
        return fileID.toString();
    }

    public String addFileCustom(CustomMultipartFile upload) throws IOException {

        // define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        // store in database which returns the objectID
        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                metadata);

        // return as a string
        System.out.println(fileID.toString());
        return fileID.toString();
    }

    public myFile getFile(String id) throws IOException {
        // Search file
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        // Step 1: Create a Concrete Builder
        myFile.Builder builder = new myFile.ConcreteBuilder();

        // Step 2: Create a Director
        myFile.Director director = new myFile.Director();

        // Step 3: Check for conditions and construct the myFile object
        myFile loadFile;
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile = director.constructFile(
                    builder,
                    gridFSFile.getFilename(),
                    gridFSFile.getMetadata().get("_contentType").toString(),
                    gridFSFile.getMetadata().get("fileSize").toString(),
                    IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        } else {
            // Handle the case when gridFSFile or its metadata is null
            loadFile = null;
        }

        return loadFile;
    }

    public myFile downloadFile(String id) throws IOException {
        // Search file
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        // Step 1: Create a Concrete Builder
        myFile.Builder builder = new myFile.ConcreteBuilder();

        // Step 2: Create a Director
        myFile.Director director = new myFile.Director();

        // Step 3: Check for conditions and construct the myFile object
        myFile loadFile;
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile = director.constructFile(
                    builder,
                    gridFSFile.getFilename(),
                    gridFSFile.getMetadata().get("_contentType").toString(),
                    gridFSFile.getMetadata().get("fileSize").toString(),
                    IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        } else {
            // Handle the case when gridFSFile or its metadata is null
            loadFile = null;
        }

        return loadFile;
    }

    public HashMap<String, String> getFiles() throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        GridFSFindIterable gridFSFiles = template.find(new Query());

        for (GridFSFile gridFSFile : gridFSFiles) {
            if (gridFSFile != null && gridFSFile.getMetadata() != null) {
                String id = gridFSFile.getId().toString().split("value=")[1];
                id = id.substring(0, id.length() - 1);
                GridFSFile f = template.findOne(new Query(Criteria.where("_id").is(id)));
                map.put(id, f.getFilename());
            }
        }

        return map;
    }

    public void deleteFile(String id) {
        try {
            template.delete(new Query(Criteria.where("_id").is(id)));
        } catch (Exception e) {

        }
    }

    public void deleteAll() {
        try {
            template.delete(new Query());
        } catch (Exception e) {

        }
    }
}