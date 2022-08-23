#!/usr/local/bin/perl

require 'midibset.pl';

# postされたデータを読み込む
$read = 0;
binmode STDIN;
read(STDIN,$data,8);
$read = $read + 8;
$type = $data;
if ($type eq '323DIA01' || $type eq '323DIA02') {
	# 保存するぜ！

	# アプレットからのデータを受け取る
	if ($type eq '323DIA02') {
		# ヘッダ
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
	# MIDIファイル
	read(STDIN,$data,4);
	$read = $read + 4;
	@data = unpack("C4", $data);
	$size = $data[0]+$data[1]*256+$data[2]*65536+$data[3]*16777216;
	read(STDIN,$data,$size);
	$read = $read + $size;
	if ($data!~/^MThd\x00\x00\x00\x06/ || $data!~/MTrk/) { &error('受信したデータはMIDIではありません！'); }
	$mididata = $data;
	# MIFUMIDIAファイル
	read(STDIN,$data,4);
	$read = $read + 4;
	@data = unpack("C4", $data);
	$size = $data[0]+$data[1]*256+$data[2]*65536+$data[3]*16777216;
	read(STDIN,$data,$size);
	$read = $read + $size;
	$mmadata = $data;
	# データチェック
	if ($read!=$ENV{'CONTENT_LENGTH'}) { &error('データサイズが合いません！'); }

	# 受け取ったデータを保存しアプレットに結果を返す
	if ($in{'action'} ne 'replace') {
		# 新規投稿らしい

		# 一時保存ディレクトリ作成
		srand();
		$retry = $setting_retry;
		do {
			if ($retry<0) { &error('データがセーブできませんでした！'); }
			$id = int(rand(100000000));
			$retry--;
		} until(mkdir($setting_tempdir.$id, 0755));
		$filename = $setting_tempdir.$id.'/temp';
		# 保存
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
		# 差し替えらしい
		&error('差し替えにはまだ対応していません！');
	}
	exit;
}else {
	&error('不明なデータ形式です！');
}

sub error {
	print "Content-type: text/html\n\nError: $_[0]";
	exit;
}
