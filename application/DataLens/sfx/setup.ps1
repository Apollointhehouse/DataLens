$desktop = [Environment]::GetFolderPath("Desktop");
$shortcut = Join-Path $desktop "DataLens.lnk";
$installDir = "$env:USERPROFILE\\DataLens";
$temp = Get-Location

$s=(New-Object -COM WScript.Shell).CreateShortcut("$shortcut");
$s.TargetPath="$installDir\\run.bat";
$s.Save();

New-Item -Path "$installDir" -ItemType Directory
Copy-Item -Path temp -Destination $installDir -Recurse -Force;