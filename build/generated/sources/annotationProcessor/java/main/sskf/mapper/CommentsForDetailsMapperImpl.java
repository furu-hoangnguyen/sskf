package sskf.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sskf.model.entity.CommentsForDetailsEntity;
import sskf.model.request.CommentDetailsRequest;
import sskf.model.response.CommentDetailResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.6 (Oracle Corporation)"
)
@Component
public class CommentsForDetailsMapperImpl extends CommentsForDetailsMapper {

    @Override
    public CommentsForDetailsEntity toEntity(CommentDetailsRequest commentDetailsRequest) {
        if ( commentDetailsRequest == null ) {
            return null;
        }

        CommentsForDetailsEntity commentsForDetailsEntity = new CommentsForDetailsEntity();

        commentsForDetailsEntity.setCd( commentDetailsRequest.getCd() );
        commentsForDetailsEntity.setIsDeputy( commentDetailsRequest.getIsDeputy() );
        commentsForDetailsEntity.setComment( commentDetailsRequest.getComment() );

        return commentsForDetailsEntity;
    }

    @Override
    public CommentDetailResponse toResponse(CommentsForDetailsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommentDetailResponse commentDetailResponse = new CommentDetailResponse();

        commentDetailResponse.setCd( entity.getCd() );
        commentDetailResponse.setIsDuplicated( entity.getIsDuplicated() );
        commentDetailResponse.setIsCapableOfBeingDeleted( entity.getIsCapableOfBeingDeleted() );
        commentDetailResponse.setShainNm( entity.getShainNm() );
        commentDetailResponse.setBumonNm( entity.getBumonNm() );
        commentDetailResponse.setIsDeputy( entity.getIsDeputy() );
        commentDetailResponse.setComment( entity.getComment() );
        commentDetailResponse.setCreatedAt( entity.getCreatedAt() );

        return commentDetailResponse;
    }
}
