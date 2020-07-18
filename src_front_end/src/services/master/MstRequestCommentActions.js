import http from '@/http-common';

const MstRequestCommentActions = {

    getRequestCommentActions(requestCd) {
        return http.get(`/mst-request-comment-actions/${requestCd}`);
    },

}

export default MstRequestCommentActions;