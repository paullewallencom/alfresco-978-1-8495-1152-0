%echo off

set CONF=Debug

echo *********************
echo * Cleaning solution *
echo *********************

msbuild AwsSamples.sln /property:Configuration=%CONF% /t:rebuild

echo *********************
echo * Building solution *
echo *********************

msbuild AwsSamples.sln /property:Configuration=%CONF%

echo *****************
echo * Running tests *
echo *****************

libs\nunit-console.exe src\Samples\bin\%CONF%\Samples.dll
cd ..

echo ********
echo * Done *
echo ********