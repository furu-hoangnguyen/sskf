package sskf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailModel {
    private Set<String> emailAddresses;
    private String subject;
    private String textBody;
    private String htmlBody;
    public final static String signature = "\n" +
            "-----------------------------------------------------------------------------------------------------------\n" +
            "\n" +
            "自動配信メールのため、当メールアドレスへの返信はできません。";

    private String hostName;

    public EmailModel(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public EmailModel buildEmailForCreatedAccountReceivable(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が入力を行いました。\n" +
                "\n" +
                "確認が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForCreatePromotion(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が入力を行いました。\n" +
                "\n" +
                "申請が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForConfirm(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "請求情報の確認が行われました。\n" +
                "\n" +
                "申請が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApply(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼リマインド";
        this.textBody = "3日間変更が無い請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApplierWhenApplyRequest(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が申請を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApprovedWhenApplyRequest(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が申請を行いました。\n" +
                "\n" +
                "承認が必要な請求情報があります。 \n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForRejectedRequest(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が却下を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApproverSendBackRequest(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が差し戻しを行いました。\n" +
                "\n" +
                "申請もしくは承認が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。 \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApproverIsSkippedSendBackRequest(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が差し戻しを行い、自身の承認がスキップされました。" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApproverOtherSendBackRequest(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が差し戻しを行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForConfirmSendBack(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が差し戻し確認を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }
    public EmailModel buildEmailForApproverWhenConfirmSendBack(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が差し戻し確認を行い、自身の承認がスキップされました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForConfirmPersonWhenRequestSalesEdit(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が修正依頼を行いました。\n" +
                "\n" +
                "確認が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApplicantWhenRequestSalesEdit(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が修正依頼を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForNormalApproverApprovedToHigherLevel(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が承認を行いました。\n" +
                "\n" +
                "承認が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForNormalApproverApprovedToSameLevel(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が承認を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForNormalApproverApprovedToLowerSkipLevel(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が承認を行い、自身の承認がスキップされました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForAccountingCharge(Long requestCd) {
        this.subject = "[未収金処理システム]作業依頼";
        this.textBody = "(人物名)が決裁を行いました。\n" +
                "\n" +
                "決済の確認が必要な請求情報があります。\n" +
                "\n" +
                "次のURLをクリックし、確認してください。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForApplicantWhenSettlementApproved(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が決裁を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    public EmailModel buildEmailForSettlementedRequest(Long requestCd) {
        this.subject = "[未収金処理システム]通知";
        this.textBody = "(人物名)が決済確認を行いました。\n" +
                "\n" +
                "次のURLをクリックし、確認出来ます。\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "▼URL\n" +
                "\n" +
                getURL(requestCd);
        return this;
    }

    private String getURL(Long requestCd) {
        return this.hostName + "/request-details?request-cd=" + requestCd;
    }
}
