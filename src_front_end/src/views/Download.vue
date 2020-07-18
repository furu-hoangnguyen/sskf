<template>
   <b-overlay :show="isProgressing" rounded="lg" opacity="0">
    <template v-slot:overlay>
      <div class="d-flex align-items-center mt-5">
        <i class="fas fa-circle-notch fa-spin"></i>
      </div>
    </template>
   </b-overlay>
</template>
<script>
import FileService from "@/services/FileService";
import includes from "lodash/includes";
export default {
  name: "Download",
  data() {
    return {
      isProgressing: true
    }
  },
  created() {
    if(this.$route.query && this.$route.query.awsS3FilePath) {
      let file = this.$route.query;
      let listTypeCanView = [".pdf", ".jpg", ".jpeg", ".JPG", ".JPEG", ".jpe", ".jfif", ".pjpeg", ".pjp", ".png", ".gif", ".svg", ".svgz", ".tif", ".tiff", ".bmp"];
      let fileName = file.awsS3FilePath.split(".");
      let fileType = fileName[fileName.length - 1];
      if(fileType){
        fileType = fileType.toLowerCase();
      }
      let mode = "view";
      if(!includes(listTypeCanView, `.${fileType}`)) {
        mode = "download";
      }
      FileService.downloadFile(file.awsS3FilePath)
      .then(response => {
        if(mode == "view" && response.data) {
          let blob = null;
          let url = null;
          if(fileType == "pdf") {
            blob = new Blob([response.data], {type: "application/pdf"});
            url = window.URL.createObjectURL(blob);
          } else {
            blob = new Blob([response.data],{type: `image/${fileType}`});
            url = window.URL.createObjectURL(blob);
          }
          if(url) {
            const link = document.createElement('a');
            link.href = url;
            document.body.appendChild(link);
            link.click();
          }
        } else if(mode == "download" && response.data) {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', file.fileOriginalName);
          document.body.appendChild(link);
          link.click();
        }
      })
      .catch(err => {
        console.log(err);
      })
      .then(() => {
        this.isProgressing = false
      });
    }
  }
}
</script>