# �ݒ�ł���I

# �X�N���v�g�{�̂ł���I
$setting_script = './midib.cgi';
# MIDI��M�X�N���v�g�ł���I
$setting_captor = './save.cgi';
# cgi-lib.pl�̏ꏊ�ł���I
$setting_cgilib = '../cgi-lib.pl';

# �f�����O�ł���I
$setting_savefile = './midib.txt';
# MIDI�ۊǌɂł���I
$setting_mididir = './midi/';
# MIDI�ꎞ�ۊǌɂł���I
$setting_tempdir = './temp/';

# �~�X�����Ƃ��̃��g���C�񐔂ł���I
$setting_retry = 10;

# ���b�N�`���ł���I(0:���b�N���Ȃ�,1:mkdir�Ń��b�N)
$setting_lock = 1;
# ���b�N�f�B���N�g���ł���I
$setting_lockdir = './lock/';
# ���b�N�t�@�C���ł���I
$setting_lockfile = './lock/midib.lock';

# �P�y�[�W�̕\�������ł���I
$setting_pageview = 5;

# MIDI���K�{���ǂ������߂�ł���I
$midi_required = 1;

# �폜���ꂽ�L�����\���ɂ��邩�ǂ������߂�̂ł���I
$hide_deleted = 1;

# �Ǘ��p�p�X�ł���I
$setting_admin = 'password';


# �ȉ��A�f�U�C���ݒ�ł���I

# �S���ɋ��ʂ���w�b�_�ł���I
$setting_header =<<"EOM";
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja"><head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
EOM
# �S���ɋ��ʂ���t�b�^�ł���I
$setting_footer =<<"EOM";
</body></html>
EOM

# �g�b�v�y�[�W�̃w�b�_�ł���I($setting_header�̌�ɑ}��)
$setting_toppage_header =<<"EOM";
<title>�~�f�B�r</title>
</head><body>
<h1>�~�f�B�r</h1>
<p class=info>MIDI������BBS�A������MIDIB�B���G�`���f���̗v�̂ŃI�����C����MIDI������f���B<br>
�ȈՓI�ɂł������f�������ݑ΍���{���܂����B</p>
<ul class=navi>
<li class=navi><nobr>[<a href="$setting_script?mode=midiform">�Ȃ����</a>]</nobr></li>
<li class=navi><nobr>[<a href="description.html">����</a>]</nobr></li>
<li class=navi><nobr>[<a href="rule.html">���[��</a>]</nobr></li>
<li class=navi><nobr>[<a href="info.html">���</a>]</nobr></li>
</ul>
<form action="$setting_script" method=POST>
<dl>
EOM
# �g�b�v�y�[�W�̃t�b�^�ł���I($setting_footer�̑O�ɑ}��)
$setting_toppage_footer1 =<<"EOM";
</dl>
<div align=right>
pass:<input type=password name=password value="">
<select name=mode>
<option value="" selected>�������Ȃ�</option>
<option value="midiform">���������</option>
<option value="delete">�R�����g���폜����</option>
</select>
<input type=submit value="���s">
</div>
</form>
EOM
$setting_toppage_footer2 =<<"EOM";
<p class=navi>�~�f�B�r</p>
EOM

# �i�r�Q�[�V�����̃w�b�_���t�b�^�ł���I(�i�r�Q�[�V�����̃f�[�^�̑O��ɑ}��)
$setting_navi_header =<<"EOM";
<p class=navi>new
EOM
$setting_navi_separator =<<"EOM";
 |
EOM
$setting_navi_footer =<<"EOM";
old</p>
EOM

# �X���b�h�̃w�b�_���t�b�^�ł���I(�X���b�h�̃f�[�^�̑O��ɑ}��)
$setting_thread_header =<<"EOM";
<dt>
EOM
$setting_thread_footer =<<"EOM";
</dd>
EOM
# �ԍ��̃w�b�_���t�b�^�ł���I(�ԍ��̃f�[�^�̑O��ɑ}��)
$setting_no_header =<<"EOM";
No.
EOM
$setting_no_footer =<<"EOM";

EOM
# �^�C�g���̃w�b�_���t�b�^�ł���I(�^�C�g���̃f�[�^�̑O��ɑ}��)
$setting_title_header =<<"EOM";
EOM
$setting_title_footer =<<"EOM";
EOM
# �ԐM�����N�̃w�b�_���t�b�^�ł���I(�^�C�g���̃f�[�^�̑O��ɑ}��)
$setting_replylink_header =<<"EOM";
 [
EOM
$setting_replylink_footer =<<"EOM";

]</dt><dd>
EOM
$setting_replylink_alternative =<<"EOM";
</dt><dd>
EOM
# MIDI�̃w�b�_���t�b�^�ł���I(MIDI�̃f�[�^�̑O��ɑ}��)
$setting_midi_header =<<"EOM";
EOM
$setting_midi_footer =<<"EOM";
��ҁF
EOM
# ���O�̃w�b�_���t�b�^�ł���I(���O�̃f�[�^�̑O��ɑ}��)
$setting_name_header =<<"EOM";
EOM
$setting_name_footer =<<"EOM";
����̃R�����g
EOM
# �̃w�b�_���t�b�^�ł���I(�̃f�[�^�̑O��ɑ}��)
$setting_mail_header =<<"EOM";
 [<a href="mailto:
EOM
$setting_mail_footer =<<"EOM";
">Mail</a>]
EOM
# �̃w�b�_���t�b�^�ł���I(�̃f�[�^�̑O��ɑ}��)
$setting_url_header =<<"EOM";
 [<a href="
EOM
$setting_url_footer =<<"EOM";
">HP</a>]
EOM
# �̃w�b�_���t�b�^�ł���I(�̃f�[�^�̑O��ɑ}��)
$setting_comment_header =<<"EOM";
<br>
EOM
$setting_comment_footer =<<"EOM";
EOM
# �̃w�b�_���t�b�^�ł���I(�̃f�[�^�̑O��ɑ}��)
$setting_date_header =<<"EOM";
<div class=info>
EOM
$setting_date_footer =<<"EOM";
</div><hr>
EOM
# �폜����܂���
$setting_deleted =<<"EOM";
�폜����܂���
EOM

# MIDI�쐬�y�[�W�̃w�b�_�ł���I($setting_header�̌�ɑ}��)
$setting_midiform_header =<<"EOM";
<title>TGWS&gt;�~�f�B�r&gt;�쐬</title>
</head><body>
<h1>�Ȃ����</h1>
EOM
# MIDI�쐬�y�[�W�̃t�b�^�ł���I($setting_footer�̑O�ɑ}��)
$setting_midiform_footer =<<"EOM";
<p>���Ղ��ۂ������̉E���̃G���A�����_�u���N���b�N���ĉ�����u���܂��B<br>
�����̒������̓t�B�[���h�ɐ��������ăG���^�[�L�[�������Ɖ��̒������ς����܂�(���������P������24)�B<br>
�E�_�u���N���b�N�ŉ������������Ƃ��ł��܂��B<br>
���t�{�^���������ƁA���݈ʒu(�����c�_�̂���ʒu)���牉�t���n�܂�܂��B|&lt;�{�^���������Ă��牉�t�{�^���������ƍŏ����牉�t����܂��B<br>
���M�{�^����<a href="midi/">�ۑ�</a>����A�݂�Ȃ�������悤�ɂȂ�܂��B<br>
MIDI��GM�K�i�Ŏg�p�\\��128�̉��F���g���܂��B<br>
<a href="help/000.png" target="_blank">��ʐ���</a></p>
<p>���܂��\\������Ȃ��ꍇ��Java���s�������݂��Ȃ�������o�[�W����������Ȃ����A�Z�L�����e�B�̐ݒ��u���E�U�Ƃ̑����Ȃǂɂ��\\��������܂��B<br>
<a href="http://java.com/ja/download/">Java Runtime Environment</a>���C���X�g�[�����邩�A�u���E�U�̐ݒ�����m�F���������B</p>
<p class=navi><a href="../">TGWS</a>&gt;<a href="$setting_script">�~�f�B�r</a>&gt;�쐬</p>
EOM

# �L�����e�y�[�W�̃w�b�_�ł���I($setting_header�̌�ɑ}��)
$setting_registform_header =<<"EOM";
<title>TGWS&gt;�~�f�B�r&gt;�L�����e</title>
</head><body>
<h1>�R�����g������</h1>
EOM
# �L�����e�y�[�W�̃t�b�^�ł���I($setting_footer�̑O�ɑ}��)
$setting_registform_footer =<<"EOM";
<p>���͂������ē��e���܂��B<br>
MIDI��������ꍇ�A�����ŃR�����g�������ē��e���邱�Ƃō쐬���������܂��B</p>
<p class=navi><a href="../">TGWS</a>&gt;<a href="$setting_script">�~�f�B�r</a>&gt;�L�����e</p>
EOM

# �ԐM�y�[�W�̃w�b�_�ł���I($setting_header�̌�ɑ}��)
$setting_replyform_header =<<"EOM";
<title>TGWS&gt;�~�f�B�r&gt;�ԐM</title>
</head><body>
<h1>�ԐM����</h1>
<dl>
EOM
# �L�����e�y�[�W�̃t�b�^�ł���I($setting_footer�̑O�ɑ}��)
$setting_replyform_footer =<<"EOM";
</dl>
<p>�Ȃ𒮂������z��A���Ԏ��Ȃǂ������܂��傤�B</p>
<p class=navi><a href="../">TGWS</a>&gt;<a href="$setting_script">�~�f�B�r</a>&gt;�L�����e</p>
EOM

1;
