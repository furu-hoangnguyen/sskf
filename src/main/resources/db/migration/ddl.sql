drop database if exists ssk_accounts_receivable;
create database if not exists ssk_accounts_receivable default character set utf8mb4 default collate utf8mb4_general_ci;

USE ssk_accounts_receivable;

drop table if exists mst_bumon;
create table mst_bumon
(
    bumon_cd          varchar(4)  NOT NULL comment '部門コード',
    bumon_cd_nk       varchar(4) comment '削除フラグ',
    bumon_nm          varchar(40) NOT NULL comment '部門名',
    bumon_knm         varchar(30) NOT NULL comment '部門カナ名',
    bumon_rnm         varchar(10) NOT NULL comment '部門略称',
    batch_update_date datetime comment '基幹システムバッチ更新日時',
    created_at        datetime default current_timestamp,
    updated_at        datetime on update current_timestamp,
    primary key (bumon_cd)
) engine = innodb comment '部門マスタ';

drop table if exists shain;
create table shain
(
    shain_cd                                           varchar(6)  NOT NULL comment '社員コード',
    shain_cd_nk                                        varchar(6) comment '削除フラグ',
    shain_nm                                           varchar(20) NOT NULL comment '社員名',
    password                                           varchar(20) NOT NULL comment 'パスワード',
    bumon_cd                                           varchar(4)  NOT NULL comment '部門コード',
    batch_update_date                                  datetime comment '基幹システムバッチ更新日時',
    is_alerted_for_application                         tinyint(1) default 1 comment '申請 アラートフラグ',
    is_alerted_for_modification_request                tinyint(1) default 1 comment '修正依頼 アラートフラグ',
    is_alerted_for_rejection                           tinyint(1) default 1 comment '却下  アラートフラグ',
    is_alerted_for_approval                            tinyint(1) default 1 comment '承認 アラートフラグ',
    is_alerted_for_sending_request_back                tinyint(1) default 1 comment '差し戻し アラートフラグ',
    is_alerted_for_confirming_send_request_back        tinyint(1) default 1 comment '差し戻し確認 アラートフラグ',
    is_alerted_for_input_payment_date                  tinyint(1) default 1 comment '支払日入力 アラートフラグ',
    is_alerted_for_confirming_settlement               tinyint(1) default 1 comment '決済確認 アラートフラグ',
    is_alerted_for_application_deputy                  tinyint(1) default 1 comment '申請代理人設定 アラートフラグ',
    is_alerted_for_approval_deputy                     tinyint(1) default 1 comment '承認代理人設定 アラートフラグ',
    is_alerted_for_changing_charge                     tinyint(1) default 1 comment '担当者変更 アラートフラグ',
    is_alerted_for_being_created                       tinyint(1) default 1 comment '作成中 リマインドアラートフラグ',
    is_alerted_for_waiting_confirmation                tinyint(1) default 1 comment '確認待ち リマインドアラートフラグ',
    is_alerted_for_waiting_application                 tinyint(1) default 1 comment '申請待ち リマインドアラートフラグ',
    is_alerted_for_waiting_approval                    tinyint(1) default 1 comment '承認待ち リマインドアラートフラグ',
    is_alerted_for_waiting_application_on_sending_back tinyint(1) default 1 comment '申請待ち(差し戻し中) リマインドアラートフラグ',
    is_alerted_for_waiting_approval_on_sending_back    tinyint(1) default 1 comment '承認待ち(差し戻し中) リマインドアラートフラグ',
    is_alerted_for_waiting_confirming_settlement       tinyint(1) default 1 comment '決済確認待ち リマインドアラートフラグ',
    is_alerted_for_updating_database                   tinyint(1) default 1 comment 'マスタ変更適用 アラートフラグ',
    created_at                                         datetime   default current_timestamp,
    updated_at                                         datetime on update current_timestamp,
    primary key (shain_cd),
    foreign key (bumon_cd)
        references mst_bumon (bumon_cd)
) engine = innodb comment '社員';

drop table if exists shain_addresses;
create table shain_addresses
(
    shain_cd     varchar(6) NOT NULL comment '社員コード',
    mail_address varchar(100) comment 'メールアドレス',
    created_at   datetime default current_timestamp,
    updated_at   datetime on update current_timestamp,
    primary key (shain_cd),
    foreign key (shain_cd)
        references shain (shain_cd)
) engine = innodb comment '社員メールアドレス';

drop table if exists mst_hinmoku;
create table mst_hinmoku
(
    hinmoku_cd                varchar(7)  NOT NULL comment '品目コード',
    hinmoku_cd_nk             varchar(7) comment '削除フラグ',
    brand_kbn                 varchar(1)  NOT NULL comment 'ブランド区分 1:NB, 2:PB, 3:OEM, 4:その他',
    han_keitai_kbn            varchar(1)  NOT NULL comment '販売形態区分 0:家庭用, 1:業務用',
    hinmoku_kbn               varchar(2)  NOT NULL comment '品目区分 10:製品, 11:商品, 12:原料, 13:包材, 14:半製品, 15:セット品, 19:輸入品, 20:運賃, 21:訂正, 22:消費税, 23:値引, 24:値増, 25:販促費, 26:物流費',
    jigyo_cd                  varchar(4)  NOT NULL comment '事業コード 1000:調味料事業, 6000:飲料事業, 8000:アグリ事業, 9999:その他',
    hinmoku_knm               varchar(30) NOT NULL comment '品目カナ名',
    hinmoku_rnm               varchar(30) NOT NULL comment '品目略称',
    hinmoku_knm_for_search    varchar(30) NOT NULL comment '検索用　品目カナ名',
    hinmoku_rnm_for_search    varchar(30) NOT NULL comment '検索用　品目略称',
    kikaku                    decimal(5, 0) comment '規格',
    nisugata                  varchar(15) comment '荷姿',
    nisugata_for_search       varchar(15) comment '検索用　荷姿',
    irisu                     decimal(4, 0) comment '計算入数',
    yoryo_tani                varchar(3) comment '容量単位',
    batch_update_date_hinmoku datetime comment 'バッチ更新日時(品目マスタ)',
    update_date_aw            datetime comment '更新日時(AW)',
    update_date_mb            datetime comment '更新日時(MB)',
    mf_yen                    decimal(8, 2) comment '生産者価格',
    hyojyun_yen               decimal(8, 2) comment '標準原価(営業)',
    mishu_limit               decimal(8, 2) comment '未収上限',
    category_hin_kbn          varchar(20) comment '商品カテゴリ',
    category_hinsub_kbn       varchar(20) comment '商品サブカテゴリ',
    category_series_kbn       varchar(20) comment '商品シリーズ',
    created_at                datetime default current_timestamp,
    updated_at                datetime on update current_timestamp,
    primary key (hinmoku_cd)
) engine = innodb comment '品目マスタ';

drop table if exists mst_tanto;
create table mst_tanto
(
    tanto_cd          varchar(4) NOT NULL comment '販売担当者コード',
    shain_cd          varchar(6) NOT NULL comment '社員コード',
    bumon_cd          varchar(4) NOT NULL comment '部門コード',
    tanto_status      varchar(1) NOT NULL comment '担当者ステータス 0:有効, 9:無効',
    batch_update_date datetime comment '基幹システムバッチ更新日時',
    created_at        datetime default current_timestamp,
    updated_at        datetime on update current_timestamp,
    primary key (tanto_cd),
    foreign key (shain_cd)
        references shain (shain_cd),
    foreign key (bumon_cd)
        references mst_bumon (bumon_cd)
) engine = innodb comment '担当マスタ';

drop table if exists mst_store;
create table mst_store
(
    store_cd              varchar(17) NOT NULL comment '店舗コード',
    store_g_cd            varchar(9) comment '店舗グループコード',
    store_g_nm            varchar(40) comment '店舗グループ名',
    store_g_nm_for_search varchar(40) comment '検索用　店舗グループ名',
    store_g_tanto_cd      varchar(4) comment '店舗グループ担当者コード',
    update_date_aw        datetime comment '更新日時(AW)',
    update_date_mb        datetime comment '更新日付(MB)',
    delete_flg            varchar(1) default 0 comment '削除フラグ',
    created_at            datetime   default current_timestamp,
    updated_at            datetime on update current_timestamp,
    primary key (store_cd)
) engine = innodb comment '店舗マスタ';

drop table if exists mst_torihiki;
create table mst_torihiki
(
    torihiki_cd              varchar(9)  NOT NULL comment '取引先コード',
    torihiki_cd_nk           varchar(9) comment '削除フラグ',
    torihiki1_nm             varchar(40) NOT NULL comment '取引先名1',
    torihiki2_nm             varchar(40) comment '取引先名2',
    torihiki_rnm             varchar(30) NOT NULL comment '取引先略称',
    torihiki_rnm_for_search  varchar(30) NOT NULL comment '検索用　取引先略称',
    torihiki_rknm            varchar(20) NOT NULL comment '取引先略称カナ',
    torihiki_rknm_for_search varchar(20) NOT NULL comment '検索用　取引先略称カナ',
    seikyu_flg               varchar(1) comment '請求先フラグ',
    choai_kori_flg           varchar(1) comment '帳合先フラグ',
    torihiki_status          varchar(1)  NOT NULL comment '取引先ステータス',
    batch_update_date        datetime comment '基幹システムバッチ更新日時',
    created_at               datetime default current_timestamp,
    updated_at               datetime on update current_timestamp,
    primary key (torihiki_cd)
) engine = innodb comment '取引先マスタ';

drop table if exists mst_yakushoku;
create table mst_yakushoku
(
    cd         varchar(10) NOT NULL comment '役職コード',
    is_deleted tinyint(1) default 0 comment '削除フラグ',
    name       varchar(60) NOT NULL comment '役職名',
    created_at datetime   default current_timestamp,
    updated_at datetime on update current_timestamp,
    primary key (cd)
) engine = innodb comment '役職マスタ';

# 役職_社員マスタ
drop table if exists mst_rel_yakushoku_shain;
create table mst_rel_yakushoku_shain
(
    shain_cd     varchar(6)  NOT NULL comment '社員コード',
    yakusyoku_cd varchar(10) NOT NULL comment '役職コード',
    created_at   datetime default current_timestamp,
    updated_at   datetime on update current_timestamp,
    primary key (shain_cd, yakusyoku_cd),
    foreign key (shain_cd)
        references shain (shain_cd),
    foreign key (yakusyoku_cd)
        references mst_yakushoku (cd)
) engine = innodb comment '役職_社員マスタ';

drop table if exists mst_systems;
create table mst_systems
(
    cd         varchar(10) NOT NULL comment 'システム名称コード',
    is_deleted tinyint(1) default 0 comment '削除フラグ',
    name       varchar(60) NOT NULL comment 'システム名称',
    created_at datetime   default current_timestamp,
    updated_at datetime on update current_timestamp,
    primary key (cd)
) engine = innodb comment 'システム名称マスタ';

drop table if exists mst_approvalflows;
create table mst_approvalflows
(
    cd         varchar(10) NOT NULL comment '承認フローコード',
    is_deleted tinyint(1) default 0 comment '削除フラグ',
    name       varchar(60) NOT NULL comment 'フロー名',
    bumon_cd   varchar(4)  NOT NULL comment '対象部門コード',
    created_at datetime   default current_timestamp,
    updated_at datetime on update current_timestamp,
    primary key (cd),
    foreign key (bumon_cd)
        references mst_bumon (bumon_cd)
) engine = innodb comment '承認フローマスタ';

drop table if exists mst_approvalflow_details;
create table mst_approvalflow_details
(
    cd              varchar(10) NOT NULL comment '承認フロー詳細コード',
    is_deleted      tinyint(1) default 0 comment '削除フラグ',
    approvalflow_cd varchar(10) NOT NULL comment '承認フローコード',
    step_number     tinyint(1)  NOT NULL comment 'ステップ値',
    yakusyoku_cd    varchar(10) NOT NULL comment '役職コード',
    bumon_cd        varchar(4)  NOT NULL comment '部門コード',
    is_deputy       tinyint(1) comment '代理フラグ',
    created_at      datetime   default current_timestamp,
    updated_at      datetime on update current_timestamp,
    primary key (cd),
    foreign key (bumon_cd)
        references mst_bumon (bumon_cd),
    foreign key (yakusyoku_cd)
        references mst_yakushoku (cd),
    foreign key (approvalflow_cd)
        references mst_approvalflows (cd)
) engine = innodb comment '承認フロー詳細マスタ';

drop table if exists mst_rel_approvalflows_systems;
create table mst_rel_approvalflows_systems
(
    approvalflow_cd varchar(10) NOT NULL comment '承認フローコード',
    system_cd       varchar(10) NOT NULL comment 'システムコード',
    created_at      datetime default current_timestamp,
    updated_at      datetime on update current_timestamp,
    primary key (approvalflow_cd, system_cd),
    foreign key (approvalflow_cd)
        references mst_approvalflows (cd),
    foreign key (system_cd)
        references mst_systems (cd)
) engine = innodb comment '承認フロー_システム名称マスタ';

################################# ↑マスタ連携対象

drop table if exists imported_master_status;
create table imported_master_status
(
    cd                  int         NOT NULL auto_increment,
    imported_table_name varchar(40) NOT NULL comment '対象マスタ',
    number              tinyint     NOT NULL comment '0:取込待ち,1:取込中,2:取込済,3:エラー',
    imported_at         datetime comment '取込完了日時',
    created_at          datetime default current_timestamp,
    primary key (cd)
) engine = innodb comment 'マスタ取込状況';

drop table if exists mst_request_types;
create table mst_request_types
(
    cd         int             NOT NULL auto_increment,
    name       varchar(20)     NOT NULL comment '申請タイプ名',
    number     int(3) zerofill NOT NULL comment '日毎の決済用番号',
    created_at datetime default current_timestamp,
    primary key (cd)
) engine = innodb comment '申請タイプ';

drop table if exists mst_request_statuses;
create table mst_request_statuses
(
    cd         int         NOT NULL auto_increment,
    name       varchar(20) NOT NULL comment 'ステータス名',
    created_at datetime default current_timestamp,
    primary key (cd)
) engine = innodb comment '申請状態';

drop table if exists mst_hinmoku_categories;
create table mst_hinmoku_categories
(
    cd            int         NOT NULL auto_increment,
    category_type tinyint     NOT NULL comment 'カテゴリタイプ 0:商品カテゴリ,1:商品サブカテゴリ,2:商品シリーズ',
    name          varchar(20) NOT NULL comment 'カテゴリ名',
    created_at    datetime default current_timestamp,
    primary key (cd)
) engine = innodb comment '品目カテゴリ';

drop table if exists mst_request_comment_actions;
create table mst_request_comment_actions
(
    cd           int         NOT NULL auto_increment,
    name         varchar(20) NOT NULL comment '処理名',
    auto_comment varchar(100) comment '自動挿入文',
    created_at   datetime default current_timestamp,
    primary key (cd)
) engine = innodb comment '申請コメント処理';

drop table if exists requests;
create table requests
(
    cd                                              int            NOT NULL auto_increment,
    mst_request_type_cd                             int            NOT NULL comment '請求タイプコード',
    settlement_number                               varchar(15) comment '決済番号',
    is_deleted                                      tinyint(1) default 0 comment '削除フラグ',
    is_sent_back                                    tinyint(1) default 0 comment '差し戻しフラグ',
    status_cd                                       int            NOT NULL comment 'ステータス',
    mst_torihiki_cd                                 varchar(9)     NOT NULL comment '取引先コード',
    torihiki_nm                                     varchar(80)    NOT NULL comment '取引先名',
    billing_amount                                  decimal(12, 0) NOT NULL comment '請求金額',
    initial_billing_amount                          decimal(12, 0) NOT NULL comment '初期　請求金額',
    billing_on                                      date           NOT NULL comment '請求日',
    payment_place                                   varchar(40)    NOT NULL comment '支払場所',
    payment_method                                  tinyint        NOT NULL comment '支払い方法 0:現金支払,1:小切手(持参),2:小切手(郵送),3:銀行振込,4:売掛金相殺,5:その他',
    payment_other_method                            varchar(200) comment 'その他支払い方法',
    scheduled_payment_on                            date           NOT NULL comment '支払予定日',
    payment_on                                      date comment '支払日',
    payment_destination                             varchar(40)    NOT NULL comment '支払先名',
    bank_name                                       varchar(16)    NOT NULL comment '銀行名',
    bank_account_number                             varchar(10)    NOT NULL comment '銀行口座番号',
    bank_account_name                               varchar(40)    NOT NULL comment '銀行口座名義',
    item_total_for_eight_percent                    decimal(12, 0) comment '8％対象品目計',
    initial_item_total_for_eight_percent            decimal(12, 0) comment '初期 8％対象品目計',
    item_total_for_ten_percent                      decimal(12, 0) comment '10％対象品目計',
    initial_item_total_for_ten_percent              decimal(12, 0) comment '初期 10％対象品目計',
    consumption_tax_total_for_eight_percent         decimal(12, 0) comment '8％消費税計',
    initial_consumption_tax_total_for_eight_percent decimal(12, 0) comment '初期 8％消費税計',
    consumption_tax_total_for_ten_percent           decimal(12, 0) comment '10％消費税計',
    initial_consumption_tax_total_for_ten_percent   decimal(12, 0) comment '初期 10％消費税計',
    item_total                                      decimal(12, 0) NOT NULL comment '品目合計',
    initial_item_total                              decimal(12, 0) NOT NULL comment '初期 品目合計',
    consumption_tax_total                           decimal(12, 0) NOT NULL comment '消費税合計',
    initial_consumption_tax_total                   decimal(12, 0) NOT NULL comment '初期 消費税合計',
    total_for_eight_percent                         decimal(12, 0) comment '8%合計',
    initial_total_for_eight_percent                 decimal(12, 0) comment '初期 8%合計',
    total_for_ten_percent                           decimal(12, 0) comment '10%合計',
    initial_total_for_ten_percent                   decimal(12, 0) comment '初期 10%合計',
    total                                           decimal(12, 0) NOT NULL comment '総合計',
    initial_total                                   decimal(12, 0) NOT NULL comment '初期 総合計',
    mst_approvalflow_cd                             varchar(10) comment '承認フローコード',
    updated_status_at                               datetime   default current_timestamp comment 'ステータス変更日時',
    requested_at                                    datetime comment '初回申請日時',
    is_alerted_for_remind                           tinyint(1) default 0 comment 'リマインドアラートフラグ',
    step_number                                     tinyint comment '承認ステップ値',
    updated_at_after_accounting_check               datetime comment '決済確認後経理印刷項目編集日時',
    edit_shain_cd                                   varchar(6) comment '編集者の社員コード',
    started_edit_at                                 datetime comment '編集開始日時',
    created_at                                      datetime   default current_timestamp,
    updated_at                                      datetime,
    primary key (cd),
    foreign key (mst_request_type_cd)
        references mst_request_types (cd),
    foreign key (mst_approvalflow_cd)
        references mst_approvalflows (cd),
    foreign key (status_cd)
        references mst_request_statuses (cd),
    foreign key (edit_shain_cd)
        references shain (shain_cd),
    unique key (settlement_number)
) engine = innodb comment '申請';

drop table if exists request_numbers;
create table request_numbers
(
    cd         int(11) zerofill NOT NULL auto_increment comment '申請番号',
    request_cd int              NOT NULL comment '申請コード',
    created_at datetime default current_timestamp,
    primary key (cd),
    foreign key (request_cd)
        references requests (cd)
) engine = innodb comment '申請番号';

drop table if exists request_attachments;
create table request_attachments
(
    request_attachment_cd int           NOT NULL auto_increment,
    request_cd            int           NOT NULL comment '申請コード',
    attachment_path       varchar(1000) NOT NULL comment 'ファイルパス',
    displayed_file_name   varchar(1000) NOT NULL comment '表示用ファイル名',
    created_at            datetime default current_timestamp,
    primary key (request_attachment_cd),
    foreign key (request_cd)
        references requests (cd)
) engine = innodb comment '申請添付';


drop table if exists operation_histories;
create table operation_histories
(
    cd                            int NOT NULL auto_increment,
    request_cd                    int NOT NULL,
    mst_request_comment_action_cd int NOT NULL,
    is_done_by_system             tinyint(1) default 0 comment 'システム自動フラグ',
    bumon_nm                      varchar(40) comment '部門名',
    shain_cd                      varchar(6) comment '社員コード',
    shain_nm                      varchar(20) comment '社員名',
    parent_operation_history_cd   int comment '親のoperation_history_cd',
    is_deputy                     tinyint(1) default 0 comment '代理フラグ',
    step_number                   tinyint(1) comment '承認フロー用ステップ値',
    comment                       varchar(1000) comment 'コメント',
    created_at                    datetime   default current_timestamp,
    primary key (cd),
    foreign key (request_cd)
        references requests (cd),
    foreign key (mst_request_comment_action_cd)
        references mst_request_comment_actions (cd),
    foreign key (shain_cd)
        references shain (shain_cd),
    foreign key (parent_operation_history_cd)
        references operation_histories (cd)
) engine = innodb comment '操作履歴';

drop table if exists request_accounts_receivables;
create table request_accounts_receivables
(
    cd                int     NOT NULL auto_increment,
    request_cd        int     NOT NULL comment '申請コード',
    target_on         date    NOT NULL comment '申請対象年月',
    purpose           tinyint NOT NULL comment '用途 0:展示会出席,1:会合出席,2:協賛(販促リベートを除く),3:物品購入,4:販売未収,5:その他',
    purpose_of_others varchar(36) comment '用途その他',
    commission_type   tinyint NOT NULL comment '口銭 0:内口銭,1:外口銭',
    remarks           varchar(1000) comment '備考',
    created_at        datetime default current_timestamp,
    updated_at        datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_cd)
        references requests (cd)
) engine = innodb comment '未収金';

drop table if exists request_accounts_receivable_details;
create table request_accounts_receivable_details
(
    cd                             int         NOT NULL auto_increment,
    request_accounts_receivable_cd int         NOT NULL comment '未収金コード',
    item_number                    varchar(4)  NOT NULL comment '表示用　明細番号',
    store_g_cd                     varchar(9) comment '店舗グループコード',
    store_g_nm                     varchar(40) NOT NULL comment '店舗グループ名',
    shain_cd                       varchar(6)  NOT NULL comment '社員コード',
    shain_nm                       varchar(20) NOT NULL comment '社員名',
    bumon_cd                       varchar(4)  NOT NULL comment '部門コード',
    bumon_nm                       varchar(40) NOT NULL comment '部門名',
    is_checked                     tinyint(1) default 0 comment '確認フラグ',
    sort_number                    int         NOT NULL comment 'ソート番号',
    is_deleted                     tinyint(1) default 0,
    created_at                     datetime   default current_timestamp,
    updated_at                     datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_accounts_receivable_cd)
        references request_accounts_receivables (cd),
    foreign key (bumon_cd)
        references mst_bumon (bumon_cd),
    foreign key (shain_cd)
        references shain (shain_cd),
    unique key (request_accounts_receivable_cd, item_number)
) engine = innodb comment '未収金用全明細';

drop table if exists details_for_accounts_receivables;
create table details_for_accounts_receivables
(
    cd                                    int            NOT NULL auto_increment,
    request_accounts_receivable_detail_cd int            NOT NULL comment '未収金用全明細コード',
    type_of_item                          tinyint        NOT NULL comment '項目名 0:定番, 1:特売, 2:EDLP, 3:月間奉仕, 4:半値導入, 5:処分販売, 6:離島, 7:その他',
    hinmoku_cd                            varchar(7)     NOT NULL comment '品目コード',
    hinmoku_rnm                           varchar(30)    NOT NULL comment '品目略称',
    yoryo                                 varchar(8)     NOT NULL comment '容量',
    irisu                                 decimal(12, 0) NOT NULL comment '入数',
    initial_irisu                         decimal(12, 0) NOT NULL comment '初期 入数',
    mf_yen                                decimal(8, 2)  NOT NULL comment '生産者価格',
    initial_mf_yen                        decimal(8, 2)  NOT NULL comment '初期 生産者価格',
    commission                            decimal(12, 2) comment '口銭',
    initial_commission                    decimal(12, 2) comment '初期 口銭',
    c_s_discount                          decimal(12, 2) comment 'c/s引条件',
    initial_c_s_discount                  decimal(12, 2) comment '初期 c/s引条件',
    denpyo_net                            decimal(12, 2) NOT NULL comment '伝票NET',
    initial_denpyo_net                    decimal(12, 2) NOT NULL comment '初期 伝票NET',
    accrued_unit_price                    decimal(12, 2) NOT NULL comment '未収単価',
    initial_accrued_unit_price            decimal(12, 2) NOT NULL comment '初期 未収単価',
    final_take_unit_price                 decimal(12, 2) NOT NULL comment '最終手取単価',
    initial_final_take_unit_price         decimal(12, 2) NOT NULL comment '初期 最終手取単価',
    hyojyun_yen                           decimal(8, 2) comment '標準原価(営業)',
    initial_hyojyun_yen                   decimal(8, 2) comment '初期 標準原価(営業)',
    quantity_of_sold_items                decimal(12, 0) NOT NULL comment '販売本数',
    initial_quantity_of_sold_items        decimal(12, 0) NOT NULL comment '初期 販売本数',
    accrued_amount                        decimal(12, 0) NOT NULL comment '未収金額',
    initial_accrued_amount                decimal(12, 0) NOT NULL comment '初期 未収金額',
    sales_amount                          decimal(12, 0) NOT NULL comment '売上金額',
    initial_sales_amount                  decimal(12, 0) NOT NULL comment '初期 売上金額',
    final_marginal_profit                 decimal(12, 0) NOT NULL comment '最終限界利益額',
    initial_final_marginal_profit         decimal(12, 0) NOT NULL comment '初期 最終限界利益額',
    final_marginal_profit_ratio           decimal(12, 2) NOT NULL comment '最終限界利益率',
    initial_final_marginal_profit_ratio   decimal(12, 2) NOT NULL comment '初期 最終限界利益率',
    mishu_limit                           decimal(8, 2) comment '未収上限',
    is_being_created                      tinyint(1) default 1 comment '明細作成中・作成済みフラグ',
    created_at                            datetime   default current_timestamp,
    updated_at                            datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_accounts_receivable_detail_cd)
        references request_accounts_receivable_details (cd)
) engine = innodb comment '未収金用未収金明細';

drop table if exists details_for_promotional_expenses;
create table details_for_promotional_expenses
(
    cd                                    int            NOT NULL auto_increment,
    request_accounts_receivable_detail_cd int            NOT NULL comment '未収金用全明細コード',
    type_of_promotional_expenses          tinyint        NOT NULL comment '販促費目 0:リベート,1:マネキン代,2:チラシ代,3:エンド協賛,4:POP代,5:展示会費用,6:センターフィー,7:その他',
    type_of_input                         tinyint        NOT NULL comment '入力タイプ 0:品目コード, 1:商品カテゴリ, 2:商品サブカテゴリ, 3:商品シリーズ',
    classification                        tinyint comment '区分 0:家庭用, 1:業務用',
    brand_classification                  tinyint comment 'ブランド区分 0:NB, 1:PB, 2:OEM, 3:その他',
    category_name                         varchar(20) comment 'カテゴリ名',
    hinmoku_cd                            varchar(7) comment '品目コード',
    hinmoku_rnm                           varchar(30) comment '品目略称',
    nisugata                              varchar(15) comment '荷姿',
    accrued_amount                        decimal(12, 0) NOT NULL comment '未収金額',
    initial_accrued_amount                decimal(12, 0) NOT NULL comment '初期 未収金額',
    created_at                            datetime default current_timestamp,
    updated_at                            datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_accounts_receivable_detail_cd)
        references request_accounts_receivable_details (cd)
) engine = innodb comment '未収金用販促費明細';

drop table if exists comments_for_details;
create table comments_for_details
(
    cd                                    int           NOT NULL auto_increment,
    is_deleted                            tinyint(1) default 0 comment '削除フラグ',
    is_duplicated                         tinyint(1) default 0 comment 'コメント複製フラグ',
    is_capable_of_being_deleted           tinyint(1) default 1 comment 'コメント削除有効フラグ',
    request_accounts_receivable_detail_cd int           NOT NULL comment '未収金用全明細コード',
    operation_history_cd                  int comment '操作履歴コード',
    shain_cd                              varchar(6)    NOT NULL comment '社員コード',
    shain_nm                              varchar(20)   NOT NULL comment '社員名',
    bumon_nm                              varchar(40)   NOT NULL comment '部門名',
    is_deputy                             tinyint(1) default 0 comment '代理フラグ',
    comment                               varchar(1000) NOT NULL comment 'コメント',
    created_at                            datetime   default current_timestamp,
    primary key (cd),
    foreign key (request_accounts_receivable_detail_cd)
        references request_accounts_receivable_details (cd),
    foreign key (operation_history_cd)
        references operation_histories (cd),
    foreign key (shain_cd)
        references shain (shain_cd)
) engine = innodb comment '明細コメント';

drop table if exists request_exhibition_promotions;
create table request_exhibition_promotions
(
    cd                          int         NOT NULL auto_increment,
    request_cd                  int         NOT NULL comment '申請コード',
    subject                     varchar(50) NOT NULL comment '表題',
    purpose                     varchar(50) NOT NULL comment '用途 0:展示会、1:協賛(販促リベートを除く)、2:物品購入、3:その他',
    purpose_of_others           varchar(100) comment '用途その他',
    store_g_cd                  varchar(9) comment '店舗グループコード',
    store_g_nm                  varchar(40) comment '店舗グループ名',
    content_of_exhibition       varchar(1000) comment '内容',
    year1                       int comment '年度1',
    sales1                      decimal(12, 0) comment '売上1 (千円)',
    initial_sales1              decimal(12, 0) comment '初期 売上1 (千円)',
    sales_year_on_year1         decimal(12, 2) comment '売上前年比1',
    initial_sales_year_on_year1 decimal(12, 2) comment '初期 売上前年比1',
    marginal_profit1            decimal(12, 0) comment '限界利益額1',
    initial_marginal_profit1    decimal(12, 0) comment '初期 限界利益額1',
    year2                       int comment '年度2',
    sales2                      decimal(12, 0) comment '売上2 (千円)',
    initial_sales2              decimal(12, 0) comment '初期 売上2 (千円)',
    sales_year_on_year2         decimal(12, 2) comment '売上前年比2',
    initial_sales_year_on_year2 decimal(12, 2) comment '初期 売上前年比2',
    marginal_profit2            decimal(12, 0) comment '限界利益額2',
    initial_marginal_profit2    decimal(12, 0) comment '初期 限界利益額2',
    year3                       int comment '年度3',
    sales3                      decimal(12, 0) comment '売上3 (千円)',
    initial_sales3              decimal(12, 0) comment '初期 売上3 (千円)',
    marginal_profit3            decimal(12, 2) comment '限界利益額3',
    initial_marginal_profit3    decimal(12, 2) comment '初期 限界利益額3',
    sales_year_on_year3         decimal(12, 0) comment '売上前年比3',
    initial_sales_year_on_year3 decimal(12, 0) comment '初期 売上前年比3',
    remarks                     varchar(1000) comment '備考',
    created_at                  datetime default current_timestamp,
    updated_at                  datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_cd)
        references requests (cd)
) engine = innodb comment '展示会申請';

drop table if exists holdings_for_request_exhibition_promotion;
create table holdings_for_request_exhibition_promotion
(
    cd                              int          NOT NULL auto_increment,
    request_exhibition_promotion_cd int          NOT NULL comment '展示会申請コード',
    start_on_of_event               date         NOT NULL comment '開催日',
    end_on_of_event                 date         NOT NULL comment '終了日',
    placement_name_of_event         varchar(100) NOT NULL comment '開催場所名',
    placement_address_of_event      varchar(100) NOT NULL comment '開催場所住所',
    sort_number                     int          NOT NULL comment 'ソート番号',
    created_at                      datetime default current_timestamp,
    updated_at                      datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_exhibition_promotion_cd)
        references request_exhibition_promotions (cd)
) engine = innodb comment '展示会開催';

drop table if exists request_mannequin_promotions;
create table request_mannequin_promotions
(
    cd                               int           NOT NULL auto_increment,
    request_cd                       int           NOT NULL comment '申請cd',
    subject                          varchar(50)   NOT NULL comment '表題',
    store_g_cd                       varchar(9) comment '店舗グループコード',
    store_g_nm                       varchar(40)   NOT NULL comment '店舗グループ名',
    content_of_implementation_stores varchar(1000) NOT NULL comment '内容',
    created_at                       datetime default current_timestamp,
    updated_at                       datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_cd)
        references requests (cd)
) engine = innodb comment 'マネキン申請';

drop table if exists request_mannequin_promotion_details;
create table request_mannequin_promotion_details
(
    cd                               int            NOT NULL auto_increment,
    request_mannequin_promotion_cd   int            NOT NULL comment 'マネキン申請コード',
    hinmoku_cd                       varchar(7)     NOT NULL comment '品目コード',
    hinmoku_rnm                      varchar(30)    NOT NULL comment '品目略称',
    nisugata                         varchar(15)    NOT NULL comment '荷姿',
    irisu                            decimal(12, 0) NOT NULL comment '入数',
    initial_irisu                    decimal(12, 0) NOT NULL comment '初期 入数',
    mf_yen                           decimal(8, 2)  NOT NULL comment '生産者価格',
    initial_mf_yen                   decimal(8, 2)  NOT NULL comment '初期 生産者価格',
    hyojyun_yen                      decimal(8, 2)  NOT NULL comment '標準原価(営業)',
    initial_hyojyun_yen              decimal(8, 2)  NOT NULL comment '初期 標準原価(営業)',
    numbers_of_cases                 decimal(12, 0) NOT NULL comment '数量(ケース)',
    initial_numbers_of_cases         decimal(12, 0) NOT NULL comment '初期 数量(ケース)',
    sales_of_seihan                  decimal(12, 0) NOT NULL comment '売上(生販)',
    initial_sales_of_seihan          decimal(12, 0) NOT NULL comment '初期 売上(生販)',
    standard_marginal_profit         decimal(12, 0) NOT NULL comment '標準限界利益額(営業)',
    initial_standard_marginal_profit decimal(12, 0) NOT NULL comment '初期 標準限界利益額(営業)',
    accrued_amount                   decimal(12, 0) NOT NULL comment '未収金額',
    initial_accrued_amount           decimal(12, 0) NOT NULL comment '初期 未収金額',
    costs_of_mannequin               decimal(12, 0) NOT NULL comment 'マネキン費用',
    initial_costs_of_mannequin       decimal(12, 0) NOT NULL comment '初期 マネキン費用',
    final_marginal_profit            decimal(12, 0) NOT NULL comment '最終限界利益額',
    initial_final_marginal_profit    decimal(12, 0) NOT NULL comment '初期 最終限界利益額',
    sort_number                      int            NOT NULL comment 'ソート番号',
    created_at                       datetime default current_timestamp,
    updated_at                       datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_mannequin_promotion_cd)
        references request_mannequin_promotions (cd)
) engine = innodb comment 'マネキン明細';

drop table if exists implementation_stores_for_request_mannequin_promotion;
create table implementation_stores_for_request_mannequin_promotion
(
    cd                             int         NOT NULL auto_increment,
    request_mannequin_promotion_cd int         NOT NULL comment 'マネキン申請コード',
    store_name_of_event            varchar(50) NOT NULL comment '実施店舗',
    run_event_on                   date        NOT NULL comment '実施実施日',
    sort_number                    int         NOT NULL comment 'ソート番号',
    created_at                     datetime default current_timestamp,
    updated_at                     datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_mannequin_promotion_cd)
        references request_mannequin_promotions (cd)
) engine = innodb comment 'マネキン実施店舗';

drop table if exists approvalflow_details;
create table approvalflow_details
(
    cd          int         NOT NULL auto_increment,
    request_cd  int         NOT NULL,
    step_number tinyint     NOT NULL comment 'ステップ値',
    bumon_cd    varchar(4)  NOT NULL comment '部門コード',
    bumon_nm    varchar(40) NOT NULL comment '部門名',
    shain_cd    varchar(6)  NOT NULL comment '社員コード',
    shain_nm    varchar(20) NOT NULL comment '社員名',
    is_deputy   tinyint(1) default 0 comment '代理フラグ',
    created_at  datetime   default current_timestamp,
    updated_at  datetime on update current_timestamp,
    primary key (cd),
    foreign key (request_cd)
        references requests (cd),
    foreign key (bumon_cd)
        references mst_bumon (bumon_cd),
    foreign key (shain_cd)
        references shain (shain_cd)
) engine = innodb comment '承認フロー詳細';

SET @@GLOBAL.time_zone = '+09:00';
SET @@SESSION.time_zone = '+09:00';

