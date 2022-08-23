# 設定である！

# スクリプト本体である！
$setting_script = './midib.cgi';
# MIDI受信スクリプトである！
$setting_captor = './save.cgi';
# cgi-lib.plの場所である！
$setting_cgilib = '../cgi-lib.pl';

# 掲示板ログである！
$setting_savefile = './midib.txt';
# MIDI保管庫である！
$setting_mididir = './midi/';
# MIDI一時保管庫である！
$setting_tempdir = './temp/';

# ミスったときのリトライ回数である！
$setting_retry = 10;

# ロック形式である！(0:ロックしない,1:mkdirでロック)
$setting_lock = 1;
# ロックディレクトリである！
$setting_lockdir = './lock/';
# ロックファイルである！
$setting_lockfile = './lock/midib.lock';

# １ページの表示件数である！
$setting_pageview = 5;

# MIDIが必須かどうか決めるである！
$midi_required = 1;

# 削除された記事を非表示にするかどうか決めるのである！
$hide_deleted = 1;

# 管理用パスである！
$setting_admin = 'password';


# 以下、デザイン設定である！

# 全部に共通するヘッダである！
$setting_header =<<"EOM";
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja"><head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
EOM
# 全部に共通するフッタである！
$setting_footer =<<"EOM";
</body></html>
EOM

# トップページのヘッダである！($setting_headerの後に挿入)
$setting_toppage_header =<<"EOM";
<title>ミディビ</title>
</head><body>
<h1>ミディビ</h1>
<p class=info>MIDIを作れるBBS、だからMIDIB。お絵描き掲示板の要領でオンラインでMIDIを作れる掲示板。<br>
簡易的にですが迷惑書き込み対策を施しました。</p>
<ul class=navi>
<li class=navi><nobr>[<a href="$setting_script?mode=midiform">曲を作る</a>]</nobr></li>
<li class=navi><nobr>[<a href="description.html">説明</a>]</nobr></li>
<li class=navi><nobr>[<a href="rule.html">ルール</a>]</nobr></li>
<li class=navi><nobr>[<a href="info.html">情報</a>]</nobr></li>
</ul>
<form action="$setting_script" method=POST>
<dl>
EOM
# トップページのフッタである！($setting_footerの前に挿入)
$setting_toppage_footer1 =<<"EOM";
</dl>
<div align=right>
pass:<input type=password name=password value="">
<select name=mode>
<option value="" selected>何もしない</option>
<option value="midiform">続きを作る</option>
<option value="delete">コメントを削除する</option>
</select>
<input type=submit value="実行">
</div>
</form>
EOM
$setting_toppage_footer2 =<<"EOM";
<p class=navi>ミディビ</p>
EOM

# ナビゲーションのヘッダ＆フッタである！(ナビゲーションのデータの前後に挿入)
$setting_navi_header =<<"EOM";
<p class=navi>new
EOM
$setting_navi_separator =<<"EOM";
 |
EOM
$setting_navi_footer =<<"EOM";
old</p>
EOM

# スレッドのヘッダ＆フッタである！(スレッドのデータの前後に挿入)
$setting_thread_header =<<"EOM";
<dt>
EOM
$setting_thread_footer =<<"EOM";
</dd>
EOM
# 番号のヘッダ＆フッタである！(番号のデータの前後に挿入)
$setting_no_header =<<"EOM";
No.
EOM
$setting_no_footer =<<"EOM";

EOM
# タイトルのヘッダ＆フッタである！(タイトルのデータの前後に挿入)
$setting_title_header =<<"EOM";
EOM
$setting_title_footer =<<"EOM";
EOM
# 返信リンクのヘッダ＆フッタである！(タイトルのデータの前後に挿入)
$setting_replylink_header =<<"EOM";
 [
EOM
$setting_replylink_footer =<<"EOM";

]</dt><dd>
EOM
$setting_replylink_alternative =<<"EOM";
</dt><dd>
EOM
# MIDIのヘッダ＆フッタである！(MIDIのデータの前後に挿入)
$setting_midi_header =<<"EOM";
EOM
$setting_midi_footer =<<"EOM";
作者：
EOM
# 名前のヘッダ＆フッタである！(名前のデータの前後に挿入)
$setting_name_header =<<"EOM";
EOM
$setting_name_footer =<<"EOM";
さんのコメント
EOM
# のヘッダ＆フッタである！(のデータの前後に挿入)
$setting_mail_header =<<"EOM";
 [<a href="mailto:
EOM
$setting_mail_footer =<<"EOM";
">Mail</a>]
EOM
# のヘッダ＆フッタである！(のデータの前後に挿入)
$setting_url_header =<<"EOM";
 [<a href="
EOM
$setting_url_footer =<<"EOM";
">HP</a>]
EOM
# のヘッダ＆フッタである！(のデータの前後に挿入)
$setting_comment_header =<<"EOM";
<br>
EOM
$setting_comment_footer =<<"EOM";
EOM
# のヘッダ＆フッタである！(のデータの前後に挿入)
$setting_date_header =<<"EOM";
<div class=info>
EOM
$setting_date_footer =<<"EOM";
</div><hr>
EOM
# 削除されますた
$setting_deleted =<<"EOM";
削除されますた
EOM

# MIDI作成ページのヘッダである！($setting_headerの後に挿入)
$setting_midiform_header =<<"EOM";
<title>TGWS&gt;ミディビ&gt;作成</title>
</head><body>
<h1>曲を作る</h1>
EOM
# MIDI作成ページのフッタである！($setting_footerの前に挿入)
$setting_midiform_footer =<<"EOM";
<p>鍵盤っぽい部分の右側のエリアを左ダブルクリックして音符を置きます。<br>
音符の長さ入力フィールドに数字を入れてエンターキーを押すと音の長さが変えられます(八分音符１個＝長さ24)。<br>
右ダブルクリックで音符を消すこともできます。<br>
演奏ボタンを押すと、現在位置(黒い縦棒のある位置)から演奏が始まります。|&lt;ボタンを押してから演奏ボタンを押すと最初から演奏されます。<br>
送信ボタンで<a href="midi/">保存</a>され、みんなが聞けるようになります。<br>
MIDIのGM規格で使用可能\な128の音色が使えます。<br>
<a href="help/000.png" target="_blank">画面説明</a></p>
<p>うまく表\示されない場合はJava実行環境が存在しなかったりバージョンが足りないか、セキュリティの設定やブラウザとの相性などによる可能\性があります。<br>
<a href="http://java.com/ja/download/">Java Runtime Environment</a>をインストールするか、ブラウザの設定をご確認ください。</p>
<p class=navi><a href="../">TGWS</a>&gt;<a href="$setting_script">ミディビ</a>&gt;作成</p>
EOM

# 記事投稿ページのヘッダである！($setting_headerの後に挿入)
$setting_registform_header =<<"EOM";
<title>TGWS&gt;ミディビ&gt;記事投稿</title>
</head><body>
<h1>コメントを書く</h1>
EOM
# 記事投稿ページのフッタである！($setting_footerの前に挿入)
$setting_registform_footer =<<"EOM";
<p>文章を書いて投稿します。<br>
MIDIを作った場合、ここでコメントを書いて投稿することで作成が完了します。</p>
<p class=navi><a href="../">TGWS</a>&gt;<a href="$setting_script">ミディビ</a>&gt;記事投稿</p>
EOM

# 返信ページのヘッダである！($setting_headerの後に挿入)
$setting_replyform_header =<<"EOM";
<title>TGWS&gt;ミディビ&gt;返信</title>
</head><body>
<h1>返信する</h1>
<dl>
EOM
# 記事投稿ページのフッタである！($setting_footerの前に挿入)
$setting_replyform_footer =<<"EOM";
</dl>
<p>曲を聴いた感想や、お返事などを書きましょう。</p>
<p class=navi><a href="../">TGWS</a>&gt;<a href="$setting_script">ミディビ</a>&gt;記事投稿</p>
EOM

1;
