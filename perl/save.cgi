#!/usr/local/bin/perl

require 'midibset.pl';

# post���ꂽ�f�[�^��ǂݍ���
$read = 0;
binmode STDIN;
read(STDIN,$data,8);
$read = $read + 8;
$type = $data;
if ($type eq '323DIA01' || $type eq '323DIA02') {
	# �ۑ����邺�I

	# �A�v���b�g����̃f�[�^���󂯎��
	if ($type eq '323DIA02') {
		# �w�b�_
		read(STDIN,$data,4);
		$read = $read + 4;
		@data = unpack("C4", $data);
		$size = $data[0]+$data[1]*256+$data[2]*65536+$data[3]*16777216;
		if ($size) {
			read(STDIN,$data,$size);
			$read = $read + $size;
			$headdata = $data;
			foreach (split(/&/, $headdata)) {
				($key, $val) = split(/=/);
				$val =~ tr/+/ /;
				$val =~ s/%([a-fA-F0-9][a-fA-F0-9])/pack("C", hex($1))/eg;
				$val =~ s/&/&amp;/g;
				$val =~ s/"/&quot;/g;
				$val =~ s/</&lt;/g;
				$val =~ s/>/&gt;/g;
				$in{$key} = $val;
			}
		}else {
			$headdata = '';
		}
	}
	# MIDI�t�@�C��
	read(STDIN,$data,4);
	$read = $read + 4;
	@data = unpack("C4", $data);
	$size = $data[0]+$data[1]*256+$data[2]*65536+$data[3]*16777216;
	read(STDIN,$data,$size);
	$read = $read + $size;
	if ($data!~/^MThd\x00\x00\x00\x06/ || $data!~/MTrk/) { &error('��M�����f�[�^��MIDI�ł͂���܂���I'); }
	$mididata = $data;
	# MIFUMIDIA�t�@�C��
	read(STDIN,$data,4);
	$read = $read + 4;
	@data = unpack("C4", $data);
	$size = $data[0]+$data[1]*256+$data[2]*65536+$data[3]*16777216;
	read(STDIN,$data,$size);
	$read = $read + $size;
	$mmadata = $data;
	# �f�[�^�`�F�b�N
	if ($read!=$ENV{'CONTENT_LENGTH'}) { &error('�f�[�^�T�C�Y�������܂���I'); }

	# �󂯎�����f�[�^��ۑ����A�v���b�g�Ɍ��ʂ�Ԃ�
	if ($in{'action'} ne 'replace') {
		# �V�K���e�炵��

		# �ꎞ�ۑ��f�B���N�g���쐬
		srand();
		$retry = $setting_retry;
		do {
			if ($retry<0) { &error('�f�[�^���Z�[�u�ł��܂���ł����I'); }
			$id = int(rand(100000000));
			$retry--;
		} until(mkdir($setting_tempdir.$id, 0755));
		$filename = $setting_tempdir.$id.'/temp';
		# �ۑ�
		open(MIDI,"> $filename.mid");
		binmode MIDI;
		print MIDI $mididata;
		close(MIDI);
		open(MMA,"> $filename.mma");
		binmode MMA;
		print MMA $mmadata;
		close(MMA);
		print "Content-type: text/html\n\nok\nLocation: $setting_script?mode=registform&id=$id";
	}else {
		# �����ւ��炵��
		&error('�����ւ��ɂ͂܂��Ή����Ă��܂���I');
	}
	exit;
}else {
	&error('�s���ȃf�[�^�`���ł��I');
}

sub error {
	print "Content-type: text/html\n\nError: $_[0]";
	exit;
}
