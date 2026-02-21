#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== 팔로우 (targetUserId=2) ==="
curl -s -X POST "$BASE_URL/api/v1/follows/2" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 언팔로우 (targetUserId=2) ==="
curl -s -X DELETE "$BASE_URL/api/v1/follows/2" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 내 팔로워 목록 ==="
curl -s -X GET "$BASE_URL/api/v1/follows/followers" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 내 팔로잉 목록 ==="
curl -s -X GET "$BASE_URL/api/v1/follows/followees" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 팔로우 수 조회 ==="
curl -s -X GET "$BASE_URL/api/v1/follows/count" \
  -b cookies.txt \
  -c cookies.txt
echo ""
