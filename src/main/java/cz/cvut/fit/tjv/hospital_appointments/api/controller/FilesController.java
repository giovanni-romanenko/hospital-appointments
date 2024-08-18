//package cz.cvut.fit.tjv.hospital_appointments.api.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/files")
//public class FilesController {
//
//    @Autowired private final AmazonS3 s3Client;
//
//    @Operation(summary = "Upload a file to S3 bucket")
//    @ApiResponse(responseCode = "201", description = "File uploaded successfully")
//    @ApiResponse(responseCode = "400", description = "Invalid request")
//    @PostMapping
//    public FileDto uploadFile(
//            @Parameter(description = "File to upload", required = true)
//            @Valid
//            @RequestParam("file") MultipartFile file) throws Exception {
//
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("File cannot be empty");
//        }
//
//        String fileName = file.getOriginalFilename();
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());
//
//
//        PutObjectRequest request = new PutObjectRequest(S3Config.S3_BUCKET_NAME,
//                fileName, file.getInputStream(), metadata);
//        s3Client.putObject(request);
//
//        return new FileDto(fileName);
//    }
//
//    @Operation(summary = "Download a file from S3 bucket")
//    @ApiResponse(responseCode = "200", description = "File downloaded successfully")
//    @ApiResponse(responseCode = "404", description = "File not found")
//    @GetMapping("/{fileName}")
//    public byte[] downloadFile(@PathVariable String fileName) throws Exception {
//        if (!s3Client.doesObjectExist(S3Config.S3_BUCKET_NAME, fileName)) {
//            throw new Exception("File not found in S3 bucket");
//        }
//        return s3Client.getObject(S3Config.S3_BUCKET_NAME, fileName).getObjectContent().readAllBytes();
//    }
//}