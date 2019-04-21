package com.majorproject2k19.MajorProject2k19.controller;

import com.majorproject2k19.MajorProject2k19.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;

@RestController
public class ImageController {

    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(method = RequestMethod.GET,value = "/major/api/run")
    public String runPythonScript() {
        System.out.println("---------------In the function runPythonScript------------------");
        try {
            Process p = Runtime.getRuntime().exec("/home/abhi/major2/bin/python3 /home/abhi/Major_Project_All_Content/darkflow-master/script-yolo.py");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s="";
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "true";
    }




//    @RequestMapping(method = RequestMethod.POST,value = "/major/api/image")
//    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
//        String fileName = fileStorageService.storeFile(file);
//
//        System.out.println("In the post image method!!!!!!!!!!!!!!!!");
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        return new UploadFileResponse(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize());
//    }


    @RequestMapping(method = RequestMethod.POST,value = "major/api/image")
    public String uploadFileAsString(@RequestBody String base64image)
    {
        System.out.println("The string is here : "+base64image);
        BufferedImage img = null;
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64image);
//        try
//        {
//            img = ImageIO.read(new ByteArrayInputStream(imageBytes));
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        String path = "./uploads/image1.jpeg";
        File file = new File(path);
//        System.out.println("AAAAAAAAA-------------AAAAAAAAA"+file.getPath());




        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            System.out.println("in the while loop");
            outputStream.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Process p = Runtime.getRuntime().exec("/home/abhi/major2/bin/python3 /home/abhi/Documents/MajorProject2k19/src/main/resources/script.py");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s="";
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "true";
    }
}