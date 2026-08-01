# Git Filter-Repo Guide
## Remove Files from Git History and Recover if Needed

This guide explains how to:

1. Install `git-filter-repo`
2. Remove files from the history of the current branch
3. Verify the cleanup
4. Push the rewritten history
5. Recover (reset) to a previous commit if required

> ⚠️ **Warning**
>
> - `git filter-repo` rewrites Git history.
> - Do **not** use it on shared branches unless everyone is aware of the history rewrite.
> - Always create a backup before running it.
> - If the files contained secrets (API keys, passwords, tokens), rotate those credentials even after removing them from Git history.

---

# Prerequisites

## Check Git Version

```bash
git --version
```

---

# Install git-filter-repo

## Windows

### Option 1 (Recommended - Using pip)

Check whether Python is installed:

```bash
python --version
```

or

```bash
py --version
```

Install git-filter-repo:

```bash
pip install git-filter-repo
```

or

```bash
py -m pip install git-filter-repo
```

Verify installation:

```bash
git filter-repo --version
```

---

### Option 2 (Using Chocolatey)

```bash
choco install git-filter-repo
```

---

## macOS

```bash
brew install git-filter-repo
```

---

## Linux

Ubuntu/Debian

```bash
sudo apt install git-filter-repo
```

or

```bash
pip3 install git-filter-repo
```

---

# Before Running

## Check Current Branch

```bash
git branch --show-current
```

Example:

```text
feature/remove-property-files
```

---

## Create a Backup Branch (Recommended)

```bash
git branch backup-before-filter
```

This allows you to return to the original history if necessary.

---

# Remove Property Files from Git History

Run the following command.

```bash
git filter-repo \
  --refs refs/heads/$(git branch --show-current) \
  --path B-Service/src/main/resources/application-test.properties \
  --path B-Service/src/main/resources/application.properties \
  --invert-paths \
  --force
```

### What this command does

- Rewrites only the current branch.
- Removes the specified files from every commit.
- Preserves all other files.
- Deletes these files completely from Git history.

---

# Verify Cleanup

Check whether the files still exist in Git history.

```bash
git log -- \
  B-Service/src/main/resources/application-test.properties \
  B-Service/src/main/resources/application.properties
```

### Expected Result

No commits should be displayed.

If nothing is returned, the cleanup was successful.

---

# Push the Rewritten History

```bash
git push --force-with-lease origin $(git branch --show-current)
```

### Why use `--force-with-lease`?

It is safer than `--force`.

It prevents accidentally overwriting someone else's commits.

---

# Recovery / Reset to Previous History

If you accidentally removed the wrong files or rewrote history incorrectly, you can restore your branch.

---

## Step 1 - View Recent Reflog

```bash
git reflog --date=local -20
```

Example

```text
8209530b HEAD@{3}: commit: Add prod details
ab3456cd HEAD@{2}: filter-repo
ef789012 HEAD@{1}: reset
```

Find the commit that represents the state you want to restore.

---

## Step 2 - Verify the Commit

```bash
git show --stat 8209530b
```

Verify:

- Commit message
- Changed files
- Statistics

---

## Step 3 - Reset to That Commit

```bash
git reset --hard 8209530b
```

---

## Step 4 - Push the Restored Branch

```bash
git push --force-with-lease origin $(git branch --show-current)
```

---

# Complete Cleanup Workflow

```bash
# Check current branch
git branch --show-current

# Create backup
git branch backup-before-filter

# Remove property files from history
git filter-repo \
  --refs refs/heads/$(git branch --show-current) \
  --path B-Service/src/main/resources/application-test.properties \
  --path B-Service/src/main/resources/application.properties \
  --invert-paths \
  --force

# Verify cleanup
git log -- \
  B-Service/src/main/resources/application-test.properties \
  B-Service/src/main/resources/application.properties

# Push rewritten history
git push --force-with-lease origin $(git branch --show-current)
```

---

# Complete Recovery Workflow

```bash
# View recent history
git reflog --date=local -20

# Verify desired commit
git show --stat <commit-id>

# Reset branch
git reset --hard <commit-id>

# Push restored history
git push --force-with-lease origin $(git branch --show-current)
```

---

# Useful Git Commands

### Current branch

```bash
git branch --show-current
```

### View commit details

```bash
git show --stat <commit-id>
```

### View file history

```bash
git log -- <file-path>
```

### Check if a file is still tracked

```bash
git ls-files | grep property
```

### View recent reflog

```bash
git reflog --date=local -20
```

### Verify filter-repo installation

```bash
git filter-repo --version
```

### Create a backup branch

```bash
git branch backup-before-filter
```

---

# Best Practices

- ✅ Create a backup branch before rewriting history.
- ✅ Verify the commit with `git show` before resetting.
- ✅ Use `git push --force-with-lease` instead of `--force`.
- ✅ Verify the cleanup using `git log`.
- ✅ Rotate any secrets that were committed, even after removing them from Git history.
- ❌ Avoid rewriting the history of shared branches without coordinating with your team.
