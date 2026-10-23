# ============================================
# COMPLETE EMAIL TESTING SCRIPT
# ============================================

$API = "http://localhost:8080/api"
$temp = "$env:TEMP"

Write-Host "`n=========================================" -ForegroundColor Cyan
Write-Host "    EMAIL NOTIFICATION TEST" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# TEST 1: Backend running?
Write-Host "`n[1] BACKEND CHECK" -ForegroundColor Yellow
try {
    $r = Invoke-WebRequest "$API/verses" -UseBasicParsing -TimeoutSec 5
    Write-Host "    Backend: Running ($($r.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "    Backend NOT running!" -ForegroundColor Red
    exit
}

# TEST 2: Base email
Write-Host "`n[2] BASE EMAIL TEST" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod "$API/email/test"
    Write-Host "    $r" -ForegroundColor Green
} catch {
    Write-Host "    Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# TEST 3: Member creation email
Write-Host "`n[3] MEMBER CREATION EMAIL" -ForegroundColor Yellow
$email = "test$(Get-Random)@gmail.com"
$memberBody = @{
    email = $email
    firstName = "Test"
    lastName = "User"
    phone = "+251911111111"
    role = "MEMBER"
} | ConvertTo-Json
try {
    $result = Invoke-RestMethod "$API/auth/create-member" -Method Post -ContentType "application/json" -Body $memberBody
    Write-Host "    Created: $($result.username) ? $email" -ForegroundColor Green
    Write-Host "    Check inbox at: $email" -ForegroundColor Cyan
} catch {
    Write-Host "    Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# TEST 4: Program email
Write-Host "`n[4] PROGRAM CREATION EMAIL" -ForegroundColor Yellow
$programBody = @{
    title = "Email Test Program"
    description = "Testing email notifications"
    location = "Room 1"
    programDate = "2026-10-15"
    programTime = "14:00"
} | ConvertTo-Json
try {
    $result = Invoke-RestMethod "$API/programs" -Method Post -ContentType "application/json" -Body $programBody
    Write-Host "    Program created: $($result.title)" -ForegroundColor Green
} catch {
    Write-Host "    Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# TEST 5: Verse email
Write-Host "`n[5] VERSE ADD EMAIL" -ForegroundColor Yellow
$verseBody = @{
    reference = "Email Test 1:1"
    verseText = "Testing verse email notification"
    translation = "NIV"
} | ConvertTo-Json
try {
    $result = Invoke-RestMethod "$API/verses" -Method Post -ContentType "application/json" -Body $verseBody
    Write-Host "    Verse created: $($result.reference)" -ForegroundColor Green
} catch {
    Write-Host "    Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# TEST 6: Gift email
Write-Host "`n[6] GIFT CONFIRMATION EMAIL" -ForegroundColor Yellow
$giftBody = @{
    userId = 2
    amount = 100
    method = "TELEBIRR"
    transactionId = "TXN_TEST_$(Get-Random)"
    status = "PENDING"
} | ConvertTo-Json
try {
    $result = Invoke-RestMethod "$API/gifts" -Method Post -ContentType "application/json" -Body $giftBody
    Write-Host "    Gift created: Br $($result.amount)" -ForegroundColor Green
} catch {
    Write-Host "    Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=========================================" -ForegroundColor Cyan
Write-Host "    CHECK YOUR EMAIL INBOX" -ForegroundColor Green
Write-Host "    obayecha@gmail.com" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "`nExpected emails:" -ForegroundColor Yellow
Write-Host "  1. Test from DDU Backend" -ForegroundColor White
Write-Host "  2. Your DDU Evangelical Account" -ForegroundColor White
Write-Host "  3. New Program: Email Test Program" -ForegroundColor White
Write-Host "  4. New Bible Verse (if configured)" -ForegroundColor White
Write-Host "  5. Gift Confirmation (if configured)" -ForegroundColor White
