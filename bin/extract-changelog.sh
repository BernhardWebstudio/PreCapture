#!/usr/bin/env bash

awk -v version="$1" '/## Version / {printit = $3 == version}; printit;' "$2"
