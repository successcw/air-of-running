# Find files with trailing whitespace
for FILE in `exec git diff-index --check --cached HEAD -- | sed '/^[+-]/d' | sed -r 's/:[0-9]+:.*//' | uniq` ; do
   # Fix them!
   sed -i 's/[[:space:]]*$//' "$FILE"
      git add "$FILE"
	  done
	  exit
