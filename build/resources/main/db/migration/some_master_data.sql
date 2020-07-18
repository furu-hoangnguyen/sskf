use ssk_accounts_receivable;

insert into mst_request_types(name, number)
values ('販売未収金', 1),
       ('展示会・協賛', 1),
       ('マネキン', 1);
insert into mst_request_statuses(name)
values ('作成中'),
       ('確認待ち'),
       ('申請待ち'),
       ('承認待ち'),
       ('却下'),
       ('決済確認待ち'),
       ('支払済');
insert into mst_request_comment_actions(name, auto_comment)
values ('作成途中保存', ''),
       ('作成', '作成が完了しました。'),
       ('明細確認', ''),
       ('確認', ''),
       ('申請', ''),
       ('承認', ''),
       ('修正依頼', ''),
       ('差し戻し', ''),
       ('却下', ''),
       ('削除', '削除されました。'),
       ('担当交代', '$role$の担当者を$shain_nm$に交代しました。'),
       ('代理人追加', '$role$の代理人に$shain_nm$を追加しました。'),
       ('ファイル変更', '$displayed_file_name$が$action$されました'),
       ('承認(スキップ)', '$role$が承認を行った為、第$skipped_number$承認者がスキップされました'),
       ('差し戻し確認', '申請者が差し戻し確認を行った為、第$skipped_number$承認者がスキップされました'),
       ('決済確認', ''),
       ('支払日入力', '支払日が入力されました。¥nこれ以降、金額の変更は出来ません。'),
       ('代理人削除', '$role$の代理人として$shain_nm$を削除しました。');
