@echo on
@set SITE=http://localhost:8380
@set CURL=curl -g -i -H "Accept: application/json" -H "Content-Type: application/json"
@set HR_YELLOW=@powershell -Command Write-Host "----------------------------------------------------------------------" -foreground "Yellow"
@set HR_RED=@powershell    -Command Write-Host "----------------------------------------------------------------------" -foreground "Red"

%HR_YELLOW%
@powershell -Command Write-Host "Load dataset" -foreground "Green"
%CURL% "%SITE%/loadDataset"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find staff by office name" -foreground "Green"
%CURL% "%SITE%/staff/find/findByOfficeName?departmentName=D-Name-1"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find all offices" -foreground "Green"
%CURL% "%SITE%/offices"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find office by name" -foreground "Green"
%CURL% "%SITE%/offices/find/findByName?name=D-Name-1"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find all workers" -foreground "Green"
%CURL% "%SITE%/workers"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find worker by first name and last name" -foreground "Green"
%CURL% "%SITE%/workers/find/findByFirstNameAndLastName?firstName=EF-Name-101&lastName=EL-Name-101"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find unknown office. Receiving empty response [200 OK]" -foreground "Magenta"
%CURL% "%SITE%/offices/find/findByName?name=unknown"
@echo.

%HR_YELLOW%
@powershell -Command Write-Host "Find unknown worker. Receiving error response [404 Not Found]" -foreground "Magenta"
%CURL% "%SITE%/workers/find/findByFirstNameAndLastName?firstName=unknown&lastName=unknown"
@echo.

%HR_RED%
@powershell -Command Write-Host "FINISH" -foreground "Red"
pause