#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== 글 작성 ==="
curl -s -X POST "$BASE_URL/api/v1/posts" \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -c cookies.txt \
  -d '{"content": "안녕하세요! 첫 번째 글입니다."}'
echo ""

echo "=== 피드 조회 (최신순) ==="
curl -s -X GET "$BASE_URL/api/v1/posts?sort=latest" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 피드 조회 (인기순) ==="
curl -s -X GET "$BASE_URL/api/v1/posts?sort=popular" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 좋아요 (postId=1) ==="
curl -s -X POST "$BASE_URL/api/v1/posts/1/likes" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 좋아요 취소 (postId=1) ==="
curl -s -X DELETE "$BASE_URL/api/v1/posts/1/likes" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 리포스트 (postId=1) ==="
curl -s -X POST "$BASE_URL/api/v1/posts/1/reposts" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 댓글 목록 (postId=1) ==="
curl -s -X GET "$BASE_URL/api/v1/posts/1/comments" \
  -b cookies.txt \
  -c cookies.txt
echo ""

echo "=== 댓글 작성 (postId=1) ==="
curl -s -X POST "$BASE_URL/api/v1/posts/1/comments" \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -c cookies.txt \
  -d '{"content": "좋은 글이네요!"}'
echo ""
