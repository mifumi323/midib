<dl>
<?php
if (!isset($prefix)) $prefix = 'tmp';
if (!isset($jar)) $jar = 'MyApplet.jar';
if (!isset($addjar)) $addjar = '';
if (!isset($class)) $class='MyApplet.class';
if (!isset($archive)) $archive='*.class';
if (!isset($jpath)) $jpath = 'C:/Program Files/Java/jdk1.6.0_02/bin';
if (!isset($dir)) $dir = $prefix.time();
if (!isset($width)) $width = 640;
if (!isset($height)) $height = 480;
if (!isset($param)) $param = array();
if (!isset($version)) $version = 1.4;
if (!isset($manifest)) $manifest = false;

foreach (glob($prefix.'*', GLOB_ONLYDIR) as $rmdir) {
	if (is_dir($rmdir)) {
		unlink($rmdir.'/'.$jar);
		rmdir($rmdir);
	}
}
mkdir($dir);
foreach (glob('*.java') as $fn) {
	if (filemtime($fn)>(int)@filemtime(str_replace('.java', '.class', $fn))) {
		echo '<dt>'.$fn.'</dt>';
		echo '<dd><pre>'.str_replace("\t", '    ', htmlspecialchars(shell_exec('"'.$jpath.'/javac" -source '.$version.' '.$fn.' 2>&1'))).'</pre><dd>';
	}
}
system('"'.$jpath.'/jar" cf'.($manifest?'m ':' ').$jar.' '.$archive);
copy($jar, $dir.'/'.$jar);
?>
</dl>
<?php
echo '<applet name="myapplet" code="'.$class.'" codebase="'.$dir.'" archive="'.$jar.$addjar.'" width="'.$width.'" height="'.$height.'" mayscript>';
foreach ($param as $name => $value)
	echo '<param name="'.$name.'" value="'.$value.'">';
?>
</applet>
