<template>
  <b-container fluid class="no-gutters px-0 upload-zone">
      <b-row class="no-gutters attachFile" v-if="!disabled" key="uploadFiles">
        <b-col md="12" lg="6" class="attachFile_body p-0">
          <validation-provider
            :immediate="false"
            v-slot="{invalid,validated}"
            :rules="{max_value: 1024*1024*1024*100}"
            :custom-messages="{max_value: 'アップロードファイルを確認して下さい'}"
            slim
          >
            <input type="hidden" :value="fileSize" :class="{ invalidClass: invalid && validated}"/>
          </validation-provider>
            <b-form-group
              label="" 
              label-for="files" 
              class="label_input label_textarea pr-0">
              <input 
                type="file" 
                ref="uploadFiles" 
                id="upload-files" 
                :disabled="disabled || !isCanChangeFile" 
                :accept="accept" 
                :multiple="multiple" 
                class="custom-file-input" 
                @change="setAttachFile" />
            </b-form-group>
            <b-row no-gutters class="attachFile_body_1" align-h="between">
              <b-col cols="auto"><label>ファイル添付</label></b-col>
              <b-col cols="auto" class="attachFile_body_btn">
                <label for="upload-files" :class="['btn btn-none', {'disabled': !isCanChangeFile}]">
                  <!-- <b-button
                    variant="none"> -->
                    ファイルを<br>選択する
                  <!-- </b-button> -->
                </label>
              </b-col>
              <b-col cols="auto" class="drop-drag-zone" align-self="center">
                <label class="note">ここにファイルを<br>ドラッグ&ドロップ</label>
              </b-col>
            </b-row>
          <!-- </validation-provider> -->
        </b-col>
        <b-col md="12" lg="6" class="attachFile_filename">
          <div class="customScrollBar">
            <b-row class="mt-1 file-container" no-gutters v-for="(file, index) in listToView" :key="`${file.name ? file.name : file.fileOriginalName}-${index}`">
              <b-col md="auto" class="px-3 file">
                <i class="fas fa-file"></i>
                <a href="javascript:void(0)" @click.stop="openFile(file)">{{file.name ? file.name : file.fileOriginalName}}</a>
              </b-col>
              <b-col md="auto" class="icon" v-if="isCanChangeFile">
                <i class="fas fa-minus-circle" :disabled="disabled" @click.stop="showPopupDelete(index)"></i>
              </b-col>
            </b-row>
          </div>
        </b-col>
      </b-row>
      <b-row class="no-gutters view" v-else key="viewListFiles">
        <b-col md="auto" class="p-0">
          <div class="form-group label_input" role="group">
            <label>ファイル添付</label>
          </div>
        </b-col>
        <b-col md="auto" class="attachFile_filename">
          <b-row v-for="file in files"
            :key="file.requestAttachmentCd"
            no-gutters
          >
            <b-col md="auto" class="px-3">
              <i class="fas fa-file"></i>
              <a href="javascript:void(0)" @click.stop="openFile(file)">{{file.name ? file.name : file.fileOriginalName}}</a>
            </b-col>
          </b-row>
        </b-col>
      </b-row>
      <DeleteDialog v-if="isShowPopupDelete" v-model="isShowPopupDelete" title="アップロードファイルを削除しますか？" @delete="deleteAttachFile" />
  </b-container>
</template>
<script>
import isTablet from "@/helper/isTabletDevice";
import FileService from "@/services/FileService";
import forEach from "lodash/forEach";
import includes from "lodash/includes";
import findIndex from "lodash/findIndex";
import getStatusOfPageDetailRequest from "@/mixins/getStatusOfPageDetailRequest";
export default {
  name: "UploadFiles",
  mixins: [getStatusOfPageDetailRequest],
  props: {
    files: {
      type: Array,
      default: () => []
    },
    multiple: {
      type: Boolean,
      default: true
    },
    accept: {
      type: String,
      default: () => {
        return ".pdf, .csv, .jpg, .jpeg, .JPG, .JPEG, .jpe, .jfif, .pjpeg, .pjp, .png, .gif, .svg, .svgz, .tif, .tiff, .bmp, .doc, .docm, .docx, .dot, .dotx, .xls, .xlsx, .xdw";
      }
    },
    rules: {
      type: Object,
      default: undefined
    },
    messages: {
      type: Object,
      default: undefined
    },
    disabled: {
      type: Boolean,
      default: false
    },
    requestStatus: {
      type: String,
      default: ""
    },
  },
  data() {
    return {
       isShowPopupDelete: false,
       selectedFileIndx: -1,
       mapFileWithName: {}
    }
  },
  computed: {
    listToView() {
      let list = [];
      let checkDupplicateSet = new Set();
      forEach(this.files, item => {
        let name = item.name ? item.name : item.fileOriginalName;
        if(!checkDupplicateSet.has(name)) {
          checkDupplicateSet.add(name);
          list.push(item);
        }
      });
      return list;
    },
    fileSize() {
      if(!this.files || !Array.isArray(this.files)) {
        return 0;
      }
      let size = this.files.reduce((sumSize, file) => {
        let fileSize = file.size || 0;
        return sumSize + fileSize;
      }, 0);
      return size || 0;
    },
    isCanChangeFile() {
      return this.statusPage == 2 || this.statusPage == 4 || this.statusPage == 6 || this.statusPage == 9;
    }
  },
  methods: {
    setAttachFile(event) {
      if(event) {
        let files = event.target.files;
        for(let i=0; i<files.length; i++) {
          let name = files[i].name;
          let fileNameWithExt = name.split(".");
          let extFile = fileNameWithExt.length > 0 ? fileNameWithExt[fileNameWithExt.length - 1] : undefined;
          if(extFile && includes(this.accept, `.${extFile.toLowerCase()}`)) {
            let replaceIndx = findIndex(this.files, item => { return item.name == name || item.fileOriginalName == name});
            this.$emit("set-attach-file-for-request", files[i], replaceIndx);
          }
        }
        setTimeout(() => {
          this.$refs.uploadFiles.value = "";
        }, 100);
      }
    },
    showPopupDelete(index) {
      this.selectedFileIndx = index;
      this.isShowPopupDelete = true;
    },
    deleteAttachFile() {
      if(this.files[this.selectedFileIndx].requestAttachmentCd) {
        FileService.deleteFile(this.files[this.selectedFileIndx].requestAttachmentCd)
        .then(response => {
          this.$emit("remove-file", this.selectedFileIndx);
        })
        .catch(err => {
          console.log(err);
        })
        .then(() => {
          this.isShowPopupDelete = false;
        })
      } else {
        this.$emit("remove-file", this.selectedFileIndx);
        this.isShowPopupDelete = false;
      }
    },
    openFile(file) {
      if(file && file.awsS3FilePath) {
        let route = this.$router.resolve(`/files?awsS3FilePath=${file.awsS3FilePath}&fileOriginalName=${file.fileOriginalName}`);
        window.open(route.href, '_blank');
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.upload-zone {
  margin-left: 0;
  & > div.row {
    height: 100%;
    /deep/ .attachFile_body {
      height: 7rem;
      max-height: 7rem;
      position: relative;
      .label_textarea {
        border: none;
        height: auto;
        width: 100%;
        position: absolute;
        top: 0;
        bottom: 0;
        right: 0;
        left: 0;
        .b-form-file {
          height: 100%;
        }
      }
      .attachFile_body_1 {
        height: 100%;
        display: flex;
        flex-wrap: nowrap;
        max-width: 100%;
        & > div{
          &.attachFile_body_btn {
            display: flex;
            align-items: center;
            margin: 0 2rem;
            label {
              &.btn {
                width: 7.5rem;
                height: 4.125rem;
                font-size: 1rem;
                padding: 8px 17.5px;
                background-color: #44a5cc;
                color: white;
                border: none;
                margin: 0.5rem 0;
              }
              &.disabled {
                cursor: not-allowed;
              }
            }
          }
          &.drop-drag-zone {
            flex-grow: 1;
          }
        }
      }
      input[type="file"] {
        height: 100%;
        &:disabled {
          cursor: not-allowed;
        }
      }
      input[type="hidden"].invalidClass {
        display: initial;
        & ~ .attachFile_body_1 > div:first-child > label{
          background: #df6561;
        }
      }
    }
    /deep/ .attachFile_filename > div {
      height: 12rem;
      max-height: 12rem;
      overflow: hidden auto;
      .file-container {
        display: flex;
        flex-wrap: nowrap;
        align-items: center;
        .file {
          display: flex;
          flex-wrap: nowrap;
          a {
            color: #44a5cc;
            font-size: 0.8rem;
            text-decoration: underline;
            margin: 0 .5rem;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }
        i {
          font-size: inherit;
        }
      }
    }
    &.view {
      .form-group.label_input {
        max-width: 9rem;
        height: 2rem;
      }
      .attachFile_filename {
        color: #44a5cc;
        min-height: 6rem;
        i.fa-file {
          margin-right: 3px;
        }
        & > div {
          height: auto;
        }
      }
    }
  }
}
</style>