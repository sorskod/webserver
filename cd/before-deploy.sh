#!/usr/bin/env bash

if [[ -z "$encrypted_23c8dfef3c63_key" || -z "$encrypted_23c8dfef3c63_iv" ]]
  then
    echo "Variables for OpenSSL are not declared."
    exit 1;
fi

openssl aes-256-cbc -K $encrypted_23c8dfef3c63_key -iv $encrypted_23c8dfef3c63_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
gpg --batch --fast-import cd/codesigning.asc
