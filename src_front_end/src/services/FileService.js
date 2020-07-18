import http from "../http-common";
var FileService = {
    downloadFile(awsFilePath, config) {
      if(!config) {
        config = {};
      }
      config.responseType = "arraybuffer";
      return http.get(`/files?awsFilePath=${awsFilePath}`, config);
    },
    deleteFile(requestAttachmentCd) {
      return http.delete(`/files/${requestAttachmentCd}`);
    }
}

export default FileService;
