#!/usr/local/bin/perl

require 'midibset.pl';
require $setting_cgilib;

&init;
&ReadParse();

if ($in{'mode'} eq 'midiform') { &midiform; }
elsif ($in{'mode'} eq 'registform') { &registform; }
elsif ($in{'mode'} eq 'regist') { &regist; }
elsif ($in{'mode'} eq 'replyform') { &replyform; }
elsif ($in{'mode'} eq 'reply') { &reply; }
elsif ($in{'mode'} eq 'delete') { &delete; }
else { &toppage; }

# �P�Ƃœ������(���̃T�u�v���V�[�W������Ă΂�Ȃ�)

sub toppage {
    &header;
    print $setting_header;
    print $setting_toppage_header;
    open(IN,$setting_savefile) || &error('���O���J���܂���ł����B');
    $lines=0;
    $start = $in{'page'}*1; $end = $start+$setting_pageview;
    while (<IN>) {
        s/<>(\r|\n)*$//;
        @data = split(/<>/);
        if ($hide_deleted) {
            $deleted = $data[9];
            unless (length($deleted)<3) { next; }
        }
        $no = shift(@data);
        $midi = shift(@data);
        $title = shift(@data);
        $lines++;
        if ($lines<=$start||$end<$lines) { next; }
        print $setting_thread_header;
        print "$setting_no_header$no$setting_no_footer";
        print "$setting_title_header$title$setting_title_footer";
        $res=0;
        while (defined($name = shift(@data))) {
            $mail = shift(@data);
            $url = shift(@data);
            $comment = shift(@data);
            $password = shift(@data);
            $date = &getdate(shift(@data));
            $deleted = shift(@data);
            if (length($deleted)<3) {
                if ($res==0) {
                    print "$setting_replylink_header<a href=\"$setting_script?mode=replyform&amp;no=$no\">�ԐM</a>$setting_replylink_footer";
                    if ($midi!=0) {
                        $midifile = $setting_mididir.$no.'.mid';
                        if (-e $midifile) {
                            $midi = "<embed src=\"$midifile\" autostart=false width=200 height=50 align=left><a href=\"$midifile\">��</a>";
                            print "$setting_midi_header$midi$setting_midi_footer";
                        }
                    }
                }
                print "$setting_name_header$name$setting_name_footer";
                print "$setting_mail_header$mail$setting_mail_footer" if ($mail);
                print "$setting_url_header$url$setting_url_footer" if ($url);
                print "<input type=radio name=no value=\"$no.$res\">";
                print "$setting_comment_header$comment$setting_comment_footer";
                print "$setting_date_header$date$setting_date_footer";
            }else {
                if (!$hide_deleted) {
                    if ($res==0) {
                        print $setting_replylink_alternative;
                    }
                    print "$setting_deleted_header$deleted$setting_deleted_footer";
                    print "<input type=radio name=no value=\"$no.$res\">";
                    print "<hr>";
                }
            }
            $res++;
        }
        print $setting_thread_footer;
    }
    close(IN);
    print $setting_toppage_footer1;
    print $setting_navi_header;
    if ($start>0) {
        print '<a href="'.$setting_script.'?page='.($start-$setting_pageview).'">&lt;</a>'.$setting_navi_separator;
    }else{
        print '&lt;'.$setting_navi_separator;
    }
    for ($_=0; $_<$lines; $_+=$setting_pageview) {
        if ($_!=$start) {
            print '<a href="'.$setting_script.'?page='.$_.'">'.($_/$setting_pageview).'</a>'.$setting_navi_separator;
        }else{
            print ''.($_/$setting_pageview).$setting_navi_separator;
        }
    }
    if ($end<$lines) {
        print '<a href="'.$setting_script.'?page='.$end.'">&gt;</a>';
    }else{
        print '&gt;';
    }
    print $setting_navi_footer;
    print $setting_toppage_footer2;
    print $setting_footer;
}

sub midiform {
    if ($in{'no'} ne '' && $in{'password'}) {
        ($no0, $res0) = split(/\./, $in{'no'});
        $error = '�Y������L��������܂���B';
        open(IN,$setting_savefile) || &error('���O���J���܂���ł����B');
        while (<IN>) {
            s/<>(\r|\n)*$//;
            @data = split(/<>/);
            $no = shift(@data);
            next if ($no!=$no0);
            $midi = shift(@data);
            $title = shift(@data);
            if ($midi!=0) {
                $mmafile = $setting_mididir.$no.'.mma';
                if (-e $mmafile) {
                }else {
                    $error = 'MIFUMIDIA�t�@�C�����J���܂���ł����B';
                    last;
                }
            }else {
                $error = 'MIDI�t�@�C�����Y�t����Ă��܂���B';
                last;
            }
            $name = shift(@data);
            $mail = shift(@data);
            $url = shift(@data);
            $comment = shift(@data);
            $password = shift(@data);
            if ($password eq '') {
                $error = '�p�X���[�h�܂��͋L���ԍ����Ԉ���Ă��܂��B';
                last;
            }
            if (crypt($in{'password'}, $password) ne $password) {
                $error = '�p�X���[�h�܂��͋L���ԍ����Ԉ���Ă��܂��B';
                last;
            }
            $date = &getdate(shift(@data));
            $deleted = shift(@data);
            if (length($deleted)<3) {
            }else {
                $error = '�폜����Ă��邽�ߕҏW�ł��܂���B';
                last;
            }
            $error = '';
            last;
        }
        close(IN);
        if ($error ne '') {
            &error($error);
        }
        open(MMA,$mmafile) || &error('MIFUMIDIA�t�@�C�����J���܂���ł����B');
        $backupdata = <MMA>;
        close(MMA);
    }
    &header;
    print $setting_header;
    print $setting_midiform_header;
    print <<"EOM";
<applet code="MIDIApplet.class" archive="MIFUMIDIA.jar" width=500 height=400>
<param name=url_exit value="./">
<param name=url_save value="$setting_captor">
<param name=url_language value="lang/japanese1.txt">
<param name=format value="323DIA01">
<param name=data value="$backupdata">
���̏ꏊ�ɃA�v���b�g���\\������܂��B<br>
</applet>
EOM
    print $setting_midiform_footer;
    print $setting_footer;
}

sub registform {
    if ($midi_required && $in{'id'} eq '') { &error('���݂͂̂̓��e�͎󂯕t���Ă���܂���B'); }
    &header;
    print $setting_header;
    print $setting_registform_header;
    print "$info";
    print <<"EOM";
<form action="$setting_script" method=POST>
<table>
EOM
    $id = '';
    $midi = '�R�����g�݂̂̓��e�ł��B';
    if ($in{'id'} ne '') {
        $midifile = $setting_tempdir.$in{'id'}.'/temp.mid';
        if (-e $midifile) {
            $mmafile = $setting_tempdir.$in{'id'}.'/temp.mma';
            $id = $in{'id'};
            $midi = "<embed src=\"$midifile\" autostart=false width=200 height=50>";
        }
    }
    print <<"EOM";
<tr><td>���O</td><td><input type=text name=name value="$name"></td></tr>
<tr><td>�d���[��</td><td><input type=text name=mail value="$mail"></td></tr>
<tr><td>�z�[���y�[�W</td><td><input type=text name=url value="$url"></td></tr>
<tr><td>�薼</td><td><input type=text name=title value="$title"></td></tr>
<tr><td>�R�����g</td><td><textarea name=comment value="">$comment</textarea></td></tr>
<tr><td>�p�X���[�h</td><td><input type=password name=password value=""></td></tr>
<tr><td><input type=submit value="���e"> <input type=reset value="���Z�b�g"></td><td>$midi</td></tr>
</table>
<input type=hidden name=id value="$id">
<input type=hidden name=mode value=regist>
</form>
EOM
    print $setting_registform_footer;
    print $setting_footer;
}

sub regist {
    # ���e�`�F�b�N�ƃf�[�^�ϊ�
    if ($ENV{'REQUEST_METHOD'} ne 'POST') { &error('�s���ȓ��e�ł��I'); }
    if ($in{'name'} eq '') { &error('���O�͕K�{�ł��I'); }
    if ($in{'title'} eq '') { &error('�^�C�g���͕K�{�ł��I'); }
    if ($in{'comment'} eq '') { &error('�R�����g�͕K�{�ł��I'); }
    if ($midi_required && $in{'id'} eq '') { &error('���݂͂̂̓��e�͎󂯕t���Ă���܂���B'); }
    $name = $in{'name'};
    $name =~ s/\r|\n//g;
    $mail = $in{'mail'};
    $mail =~ s/\r|\n//g;
    $url = $in{'url'};
    $url =~ s/\r|\n//g;
    $title = $in{'title'};
    $title =~ s/\r|\n//g;
    $comment = $in{'comment'};
    # �����̏����͔z�z�ΏۊO����
    # if (&tgws'SPAMcheck($comment)) {
    #     if (defined(%tgws'ret_prohibitedword)||defined(%tgws'ret_ngword)) {
    #         $info .= "<p>�ȉ��̕s�K�؂Ȍ�傪����܂����B�\\�������߂ē��e���Ȃ����Ă��������B</p><ul>";
    #         while (($word, $num) = each(%tgws'ret_prohibitedword)) { $info .= "<li>$word</li>"; }
    #         while (($word, $num) = each(%tgws'ret_ngword)) { $info .= "<li>$word</li>"; }
    #         $info .= "</ol>";
    #     }
    #     if (!$tgws'ret_essentialword) {
    #         $info .= "<p>�{�f���͓��{���p�ł��B�Ђ炪�Ȃ��܂ޓ��{��̕��͂�p���Ă��������B</p>";
    #     }
    #     &registform;
    #     exit;
    # }
    $name .= '<!--'.$ENV{'REMOTE_ADDR'}.':'.$ENV{'REMOTE_HOST'}.'-->';
    $comment =~ s/\r\n/<br>/g;
    $comment =~ s/\r/<br>/g;
    $comment =~ s/\n/<br>/g;
    $password = &encrypt($in{'password'});

    # �ۑ�����̂��I
    &lock;
    $no = 1;
    open(IN,$setting_savefile) || &error('���O���J���܂���ł����B');
    @lines = <IN>;
    close(IN);
    # �d���`�F�b�N���L���i���o�[�v�Z
    foreach (@lines) {
        @data = split(/<>/);
        if ($name eq $data[2] && $title eq $data[5] && $comment eq $data[6]) { &error('�d�����e�ł��I'); }
        if ($no<=$data[0]) { $no = $data[0]+1; }
    }
    $time = time();
    # MIDI�ۑ�
    $midi = 0;
    if ($in{'id'} ne '') {
        $midifile = $setting_tempdir.$in{'id'}.'/temp.mid';
        if (-e $midifile) {
            $midi = 1;
            rename $midifile, $setting_mididir.$no.'.mid';
            $mmafile = $setting_tempdir.$in{'id'}.'/temp.mma';
            if (-e $mmafile) {
                rename $mmafile, $setting_mididir.$no.'.mma';
            }
            rmdir $setting_tempdir.$in{'id'};
        }
    }
    $deleted = 0;
    # ���O�ۑ�
    unshift(@lines, "$no<>$midi<>$title<>$name<>$mail<>$url<>$comment<>$password<>$time<>$deleted<>\n");
    open(OUT,'>'.$setting_savefile) || &error('���O���J���܂���ł����B');
    print OUT @lines;
    close(OUT);
    &unlock;
    # �����I������̂Ńy�[�W�ړ�
    if ($ENV{'PERLXS'} eq "PerlIS") {
        print "HTTP/1.0 302 Temporary Redirection\r\n";
        print "Content-type: text/html\n";
    }
    print "Location: $setting_script\n\n";
}

sub replyform {
    &header;
    print $setting_header;
    print $setting_replyform_header;
    print "$info";
    open(IN,$setting_savefile) || &error('���O���J���܂���ł����B');
    $lines=0;
    while (<IN>) {
        s/<>(\r|\n)*$//;
        @data = split(/<>/);
        $no = shift(@data);
        if ($no!=$in{'no'}) { next; }
        $midi = shift(@data);
        $title = shift(@data);
        print $setting_thread_header;
        print "$setting_no_header$no$setting_no_footer";
        print "$setting_title_header$title$setting_title_footer";
        print $setting_replylink_alternative;
        if ($midi!=0) {
            $midifile = $setting_mididir.$no.'.mid';
            if (-e $midifile) {
                $midi = "<embed src=\"$midifile\" autostart=false width=200 height=50 align=left><a href=\"$midifile\">��</a>";
                print "$setting_midi_header$midi$setting_midi_footer";
            }
        }
#        $res=0;
        while (defined($name = shift(@data))) {
            $mail = shift(@data);
            $url = shift(@data);
            $comment = shift(@data);
            $password = shift(@data);
            $date = &getdate(shift(@data));
            $deleted = shift(@data);
            if (length($deleted)<3) {
                print "$setting_name_header$name$setting_name_footer";
                print "$setting_mail_header$mail$setting_mail_footer" if ($mail);
                print "$setting_url_header$url$setting_url_footer" if ($url);
                print "$setting_comment_header$comment$setting_comment_footer";
                print "$setting_date_header$date$setting_date_footer";
            }else {
                print "$setting_deleted_header$deleted$setting_deleted_footer";
            }
#            $res++;
        }
        print $setting_thread_footer;
        last;
    }
    close(IN);
    print <<"EOM";
<form action="$setting_script" method=POST>
<table>
<tr><td>���O</td><td><input type=text name=name value="$name_"></td></tr>
<tr><td>�d���[��</td><td><input type=text name=mail value="$mail_"></td></tr>
<tr><td>�z�[���y�[�W</td><td><input type=text name=url value="$url_"></td></tr>
<tr><td>�R�����g</td><td><textarea name=comment value="">$comment_</textarea></td></tr>
<tr><td>�p�X���[�h</td><td><input type=password name=password value=""></td></tr>
<tr><td><input type=submit value="�ԐM"> <input type=reset value="���Z�b�g"></td><td>No.$no�ւ̕ԐM�ł��B</td></tr>
</table>
<input type=hidden name=no value=$no>
<input type=hidden name=mode value=reply>
</form>
EOM
    print $setting_replyform_footer;
    print $setting_footer;
}

sub reply {
    # ���e�`�F�b�N�ƃf�[�^�ϊ�
    if ($ENV{'REQUEST_METHOD'} ne 'POST') { &error('�s���ȓ��e�ł��I'); }
    if ($in{'name'} eq '') { &error('���O�͕K�{�ł��I'); }
    if ($in{'comment'} eq '') { &error('�R�����g�͕K�{�ł��I'); }
    $name = $in{'name'};
    $name =~ s/\r|\n//g;
    $mail = $in{'mail'};
    $mail =~ s/\r|\n//g;
    $url = $in{'url'};
    $url =~ s/\r|\n//g;
    $comment = $in{'comment'};
    # �����̏����͔z�z�ΏۊO����
    # if (&tgws'SPAMcheck($comment)) {
    #     if (defined(%tgws'ret_prohibitedword)||defined(%tgws'ret_ngword)) {
    #         $info .= "<p>�ȉ��̕s�K�؂Ȍ�傪����܂����B�\\�������߂ē��e���Ȃ����Ă��������B</p><ul>";
    #         while (($word, $num) = each(%tgws'ret_prohibitedword)) { $info .= "<li>$word</li>"; }
    #         while (($word, $num) = each(%tgws'ret_ngword)) { $info .= "<li>$word</li>"; }
    #         $info .= "</ol>";
    #     }
    #     if (!$tgws'ret_essentialword) {
    #         $info .= "<p>�{�f���͓��{���p�ł��B�Ђ炪�Ȃ��܂ޓ��{��̕��͂�p���Ă��������B</p>";
    #     }
    #     $name_ = $name;
    #     $mail_ = $mail;
    #     $url_ = $url;
    #     $comment_ = $comment;
    #     &replyform;
    #     exit;
    # }
    $name .= '<!--'.$ENV{'REMOTE_ADDR'}.':'.$ENV{'REMOTE_HOST'}.'-->';
    $comment =~ s/\r\n/<br>/g;
    $comment =~ s/\r/<br>/g;
    $comment =~ s/\n/<br>/g;
    $password = &encrypt($in{'password'});
    $deleted = 0;

    # �ۑ�����̂��I
    &lock;
    @lines = ();
    open(IN,$setting_savefile) || &error('���O���J���܂���ł����B');
    while (<IN>) {
        s/<>(\r|\n)*$//;
        @data = split(/<>/);
        $no = shift(@data);
        if ($no!=$in{'no'}) {
            push(@lines, "$_<>\n");
            next;
        }
        $time = time();
        $new = $_."<>$name<>$mail<>$url<>$comment<>$password<>$time<>$deleted<>\n";
    }
    close(IN);
    # ���O�ۑ�
    open(OUT,'>'.$setting_savefile) || &error('���O���J���܂���ł����B');
    print OUT $new;
    print OUT @lines;
    close(OUT);
    &unlock;
    # �����I������̂Ńy�[�W�ړ�
    if ($ENV{'PERLXS'} eq "PerlIS") {
        print "HTTP/1.0 302 Temporary Redirection\r\n";
        print "Content-type: text/html\n";
    }
    print "Location: $setting_script\n\n";
}

sub delete {
    if ($ENV{'REQUEST_METHOD'} ne 'POST') { &error('�s���ȓ��e�ł��I'); }
    $error = '';
    &lock;
    ($no0,$res0) = split(/\./,$in{'no'});
    @lines = ();
    open(IN,$setting_savefile) || &error('���O���J���܂���ł����B');
    while (<IN>) {
        s/<>(\r|\n)*$//;
        @data = split(/<>/);
        $no = shift(@data);
        if ($no!=$no0) {
            push(@lines, "$_<>\n");
            next;
        }
        $midi = shift(@data);
        $title = shift(@data);
        $line = "$no<>$midi<>$title<>";
        $res=0;
        while (defined($name = shift(@data))) {
            $mail = shift(@data);
            $url = shift(@data);
            $comment = shift(@data);
            $password = shift(@data);
            $date = shift(@data);
            $deleted = shift(@data);
            if (crypt($in{'password'}, $password) ne $password) {
                $error = '�p�X���[�h�܂��͋L���ԍ����Ԉ���Ă��܂��B';
                last;
            }
            if ($res==$res0) {
                $deleted = "���e�҂ɂ��폜����܂����B";
            }
            $line .="$name<>$mail<>$url<>$comment<>$password<>$date<>$deleted<>";
            $res++;
        }
        push(@lines, "$line\n");
    }
    close(IN);
    if ($error ne '') {
        &error($error);
    }
    # ���O�ۑ�
    open(OUT,'>'.$setting_savefile) || &error('���O���J���܂���ł����B');
    print OUT @lines;
    close(OUT);
    &unlock;
    # �����I������̂Ńy�[�W�ړ�
    if ($ENV{'PERLXS'} eq "PerlIS") {
        print "HTTP/1.0 302 Temporary Redirection\r\n";
        print "Content-type: text/html\n";
    }
    print "Location: $setting_script\n\n";
}

sub init {
    $header = 0;
}

# �֐�(�l��Ԃ����Ƃ�ړI�Ƃ������)

sub encrypt {
    if ($_[0] eq '') { return ''; }
    local (@salts);
    srand();
    @salts = ( 'A'..'Z', 'a'..'z', '0'..'9', '.', '/' );
    return crypt($_[0], $salts[int(rand(64))].$salts[int(rand(64))]);
}

sub getdate {
    local($sec,$min,$hour,$mday,$mon,$year) = localtime($_[0]);
    return sprintf("%02d/%02d/%02d %02d:%02d:%02d", $year-100,$mon+1,$mday, $hour,$min,$sec);
}

# �T�u�v���V�[�W��(�Ȃɂ��P�Ƃ̓�����s��)

sub header {
    if ($header==0) {
        print &PrintHeader;
        $header = 1;
    }
}

sub error {
    &header;
    print 'Error: '.$_[0];
    &unlock;
    exit;
}

sub lock {
    if ($setting_lock==1) {
        $retry = 5;
        while (!mkdir($setting_lockfile, 0755)) {
            if (--$retry <= 0) { &error('LOCK is BUSY'); }
            sleep(1);
        }
        $lockflag = 1;
    }
}

sub unlock {
    if ($setting_lock==1) {
        if ($lockflag) {
            rmdir($setting_lockfile);
            $lockflag = 0;
        }
    }
}
