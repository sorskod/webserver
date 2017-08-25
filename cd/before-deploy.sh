#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_23c8dfef3c63_key -iv $encrypted_23c8dfef3c63_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
    gpg --batch --fast-import cd/codesigning.asc
fi
