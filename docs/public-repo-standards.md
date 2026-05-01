# Public Repository Standards

This repository is public and portfolio-facing. It should show both technical progress and professional engineering habits.

## What Belongs in the Repo

- Clear README and architecture notes.
- Small issues with explicit learning goals.
- Reproducible local setup instructions.
- Tests for important behavior as features appear.
- Safe examples such as `.env.example`.
- Useful commit history and pull requests.

## What Does Not Belong in the Repo

- Secrets, tokens, passwords, private keys, or real credentials.
- Personal machine paths or IDE workspace state.
- Large generated/build artifacts.
- Real personal data.
- Cloud account identifiers unless intentionally documented as placeholders.

## Security Habits

- Prefer environment variables for local secrets.
- Commit only placeholder configuration.
- Rotate any token that may have been exposed.
- Keep dependency updates visible through pull requests.
- Add authentication and authorization before user-specific data becomes meaningful.

## Presentation Habits

- Keep tasks small enough that a reviewer can understand them quickly.
- Explain tradeoffs in docs when a decision is interesting.
- Favor a working vertical slice over a broad unfinished framework.
- Make each milestone demonstrable from the README.
