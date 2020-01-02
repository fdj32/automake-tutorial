# git remove file guide

git filter-branch --force --index-filter \
'git rm --cached --ignore-unmatch dbimport/src/main/resources/application.yml' \
--prune-empty --tag-name-filter cat -- --all

git push origin --force --all


