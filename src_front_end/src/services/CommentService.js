import http from "../http-common";
var CommentService = {
    getOldComments(storeGCd) {
      return http.get(`/comments/old-comment?storeGCd=${storeGCd}`);
    },
    getCommentsOfAnItem (itemId) {
        return http.get(`/comments?requestAccountId=${itemId}`);
    },
    deleteComment (commentId) {
        let deleteUrl = "/comments/" + commentId;
        return http.delete(deleteUrl);
    },

    listComment(param) {
        return http.get("/comments/list",{
            params: param,
        });
    },
    saveCommentForItems(requestCd, commentDetailsRequest ) {
        return http.post(`/comments/for-item/${requestCd}`, commentDetailsRequest);
    }
}

export default CommentService;
