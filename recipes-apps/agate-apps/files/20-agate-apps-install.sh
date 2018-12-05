#!/bin/bash
set -o pipefail

function run_cmd_flaggling_execution() {
  CMD="$1"
  FLAG="$2.flag"
  LOG="logger -s"
  RETRIES=5

  if [ -f "$FLAG" ]; then
    # Nothing to do; we've been here
    echo "$FLAG ran already!" | $LOG
    return
  fi

  for ((i = 0; i < RETRIES; i++)); do
    if $CMD 2>&1 | $LOG; then
      # Flag successful completion
      touch "$FLAG"
      echo "$FLAG just ran" | $LOG
      return
    fi
  done
  echo "FATAL ERROR while executing command: $CMD" | $LOG
  exit 1
}

cd /home/root/app-data || exit 1
git clone https://github.com/enactjs/agate-apps.git
cd agate-apps || exit 1
run_cmd_flaggling_execution "npm install -g @enact/cli" cli
for PKG in console copilot communication-server components; do
  (cd $PKG || exit 1; run_cmd_flaggling_execution "npm install" $PKG.install)&
done

wait

for APP in console copilot; do
  (cd $APP || exit 1; run_cmd_flaggling_execution "npm run pack" $APP.pack)&
done

wait
sync
