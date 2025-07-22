$desktop = [Environment]::GetFolderPath("Desktop");
$shortcut = Join-Path $desktop "DataLens.lnk";
$installDir = "$env:USERPROFILE\\DataLens";

$s=(New-Object -COM WScript.Shell).CreateShortcut("$shortcut");
$s.TargetPath="$installDir\\launcher\\run.bat";
$s.Save();

if (!(Test-Path -Path $installDir)) {
    New-Item -Path "$installDir" -ItemType Directory
}

Copy-Item -Path launcher -Destination $installDir -Recurse -Force;