(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-fd923dbe"],{2545:function(t,e,a){"use strict";var s=a("d4da"),r=a.n(s);r.a},a15b:function(t,e,a){"use strict";var s=a("23e7"),r=a("44ad"),i=a("fc6a"),l=a("a640"),o=[].join,n=r!=Object,u=l("join",",");s({target:"Array",proto:!0,forced:n||!u},{join:function(t){return o.call(i(this),void 0===t?",":t)}})},a2a2:function(t,e,a){"use strict";var s=a("c427"),r={getImportedSituationMasterStatus:function(t){return s["a"].get("/master/get-imported-situation-status",{params:t})},getFilterSelectOptions:function(){return s["a"].get("/master/get-shain-filter-select-option")},getDepartmentSelectOptions:function(){return s["a"].get("dropdown/department")},getEmployeeFilterSelectOptions:function(){return s["a"].get("/dropdown/category-all")},getShains:function(t){return s["a"].get("/shain/get-shains",{params:t})},getStores:function(t){return s["a"].get("/master/get-stores",{params:t})},getTantos:function(t){return s["a"].get("/master/get-tantos",{params:t})},getSupliers:function(t){return s["a"].get("/mst-torihiki/torihikis",{params:t})},getMasterApproveDetails:function(t){return s["a"].get("/master/get-approval-flows",{params:t})}};e["a"]=r},ada4:function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("section",{staticClass:"empbody"},[a("div",{staticClass:"header"},[t._v("マスタ取込状況")]),a("div",{staticClass:"controller"},[a("div",[a("p",{staticClass:"filter-title"},[t._v("検索条件:")]),a("span",[t._v(t._s(t.filterRequestAsText))])]),a("div",[a("b-button",{staticClass:"btn-white",attrs:{variant:"none"},on:{click:function(e){t.showPopupSearch=!0}}},[t._v("検索条件")]),a("b-button",{staticClass:"btn-white",attrs:{variant:"none"},on:{click:t.resetList}},[t._v("検索条件 "),a("br"),t._v("リセット")])],1)]),a("div",{staticClass:"master-table"},[a("div",{staticClass:"filter"},[a("b-form-select",{attrs:{options:[{value:"importedAt==desc",text:"取込完了日時(降順)"},{value:"importedAt==asc",text:"取込完了日時(昇順)"}]},on:{change:t.getData},model:{value:t.request.sortField,callback:function(e){t.$set(t.request,"sortField",e)},expression:"request.sortField"}}),a("b-form-select",{attrs:{options:[{value:"20",text:"20件"},{value:"50",text:"50件"}]},on:{change:t.getData},model:{value:t.request.limitNumber,callback:function(e){t.$set(t.request,"limitNumber",e)},expression:"request.limitNumber"}})],1),a("b-table",{attrs:{"show-empty":"",striped:"",hover:"",items:t.tableItems,fields:t.tableFields}}),a("b-pagination",{staticClass:"mr-5",attrs:{align:"right","total-rows":t.totalItemsPageable,"per-page":t.request.limitNumber,"first-number":0==this.request.pageNumber,"prev-class":{"d-none":0==this.request.pageNumber},"last-number":this.request.pageNumber==this.totalPage,"next-class":{"d-none":this.request.pageNumber==this.totalPage}},on:{change:function(e){return t.onPageChanged(e)}},model:{value:t.currentPage,callback:function(e){t.currentPage=e},expression:"currentPage"}})],1),a("AdminSearchDialog",{model:{value:t.showPopupSearch,callback:function(e){t.showPopupSearch=e},expression:"showPopupSearch"}},[a("div",{staticClass:"supplieName"},[t._v("検索条件 ")]),a("b-row",[a("b-col",{attrs:{cols:"9"}},[a("b-form-group",{staticClass:"label_input searchEmployee mt-5",attrs:{label:"対象マスタ","label-for":"filter_table"}},[a("b-form-select",{attrs:{options:t.optionsTableFilter},model:{value:t.filterRequest.tableName,callback:function(e){t.$set(t.filterRequest,"tableName",e)},expression:"filterRequest.tableName"}})],1)],1),a("b-col",{attrs:{cols:"9"}},[a("b-form-group",{staticClass:"label_input searchEmployee mt-3",attrs:{label:"ステータス","label-for":"filter_status"}},[a("b-form-select",{attrs:{options:[{value:"",text:""},{value:"0",text:"取込待ち"},{value:"1",text:"取込中"},{value:"2",text:"取込済"},{value:"3",text:"エラー"}]},model:{value:t.filterRequest.status,callback:function(e){t.$set(t.filterRequest,"status",e)},expression:"filterRequest.status"}})],1)],1)],1),a("b-row",{staticClass:"search_recordAdmin"}),a("b-row",{staticClass:"mt-4"},[a("b-col",{attrs:{cols:"8","align-self":"end"}},[a("b-button-group",[a("b-button",{staticClass:"btn-filter btn-white",attrs:{variant:"none"},on:{click:t.resetSearchForm}},[t._v("リセット")]),a("b-button",{staticClass:"btn-filter btn-blue",attrs:{variant:"none"},on:{click:t.onSearch}},[t._v("検索する")])],1)],1)],1)],1)],1)},r=[],i=(a("7db0"),a("a15b"),a("a2a2")),l=a("2b0e"),o={name:"ImportedSituation",data:function(){var t=this;return{currentPage:1,totalPage:0,totalItemsPageable:0,filterRequest:{tableName:"",status:""},filterRequestAsText:"",request:{sortField:"importedAt==desc",pageNumber:0,limitNumber:20,keyword:""},statusMap:["取込待ち","取込中","取込済","エラー"],showPopupSearch:!1,tableFields:[{key:"importedTableName",label:"対象マスタ",formatter:function(e){return t.optionsTableFilter.find((function(t){return t.value==e})).text},thStyle:{width:"45%"}},{key:"importedAt",label:"取込完了日時",formatter:function(t){return l["default"].moment(t).format("YYYY/MM/DD hh:mm:ss")},thStyle:{width:"30%"}},{key:"number",label:"ステータス",formatter:function(e){return t.statusMap[e]},thStyle:{width:"25%"}}],tableItems:[],optionsTableFilter:[{value:"",text:""},{value:"shain",text:"社員マスタ"},{value:"mst_hinmoku",text:"品目マスタ"},{value:"mst_store",text:"店舗マスタ"},{value:"mst_torihiki",text:"取引先マスタ"},{value:"mst_bumon",text:"部門マスタ"},{value:"mst_tanto",text:"担当マスタ"},{value:"mst_yakushoku",text:"役職マスタ"},{value:"mst_rel_yakushoku_shain",text:"役職社員中間マスタ"},{value:"mst_approvalflows",text:"承認フローマスタ"},{value:"mst_approvalflow_details",text:"承認フロー詳細マスタ"},{value:"mst_rel_approvalflows_systems",text:"システム名称承認フロー中間マスタ"},{value:"mst_systems",text:"システム名称マスタ"}]}},methods:{getData:function(){var t=this;i["a"].getImportedSituationMasterStatus(this.request).then((function(e){t.tableItems=e.data.content,t.totalItemsPageable=e.data.totalElements,t.totalPage=e.data.totalPages-1})).catch((function(t){console.log(t)}))},onPageChanged:function(t){this.request.pageNumber=t-1,this.getData()},resetSearchForm:function(){this.filterRequest.status="",this.filterRequest.tableName="",this.filterRequestAsText="",this.request.keyword=""},resetList:function(){this.currentPage=1,this.request.pageNumber=0,this.resetSearchForm(),this.getData()},onSearch:function(){var t=this,e=[],a=[];this.currentPage=1,this.request.pageNumber=0,this.filterRequest.tableName&&(e.push("importedTableName=="+this.filterRequest.tableName),a.push("対象マスタ:"+this.optionsTableFilter.find((function(e){return e.value==t.filterRequest.tableName})).text)),this.filterRequest.status&&(e.push("number=="+this.filterRequest.status),a.push("ステータス:"+this.statusMap[this.filterRequest.status])),this.request.keyword=e.join(";"),this.filterRequestAsText=a.join(" / "),this.getData(),this.showPopupSearch=!1}},created:function(){this.getData()}},n=o,u=(a("2545"),a("2877")),c=Object(u["a"])(n,s,r,!1,null,"839b96e2",null);e["default"]=c.exports},d4da:function(t,e,a){}}]);
//# sourceMappingURL=chunk-fd923dbe.174dd369.js.map