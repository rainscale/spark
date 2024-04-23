@echo off
adb wait-for-device
echo adb devices
adb devices > devices.txt

echo -------------------------------------------------
echo "adb -s sn shell begin"
for /f "skip=1" %%i in (devices.txt) do
(
start adb-shell %%i
)
echo "adb -s sn shell end"
echo -------------------------------------------------
del devices.txt
pause