CREATE TABLE trainer (
                         id         uuid         PRIMARY KEY,
                         auth0_sub  varchar(255) NOT NULL UNIQUE,
                         email      varchar(255) NOT NULL UNIQUE,
                         name       varchar(255) NOT NULL UNIQUE
);

CREATE TABLE team (
                      id         uuid         PRIMARY KEY,
                      trainer_id uuid         UNIQUE REFERENCES trainer(id) ON DELETE CASCADE,
                      name       varchar(255)
);

CREATE TABLE pokemon (
                         id           uuid         PRIMARY KEY,
                         team_id      uuid         REFERENCES team(id) ON DELETE CASCADE,
                         pokemon_id   integer      NOT NULL,
                         pokemon_name varchar(255),
                         nickname     varchar(255),
                         position     integer      NOT NULL,
                         moves        varchar(255)[],
                         types        varchar(255)[]
);

CREATE TABLE user_session (
                              session_id               varchar(255) PRIMARY KEY,
                              trainer_id               uuid REFERENCES trainer(id) ON DELETE CASCADE,
                              auth0_access_token       varchar(2000),
                              auth0_refresh_token      varchar(2000),
                              access_token_expires_at  timestamptz,
                              session_expires_at       timestamptz
);

CREATE INDEX idx_pokemon_team          ON pokemon (team_id);
CREATE INDEX idx_session_trainer       ON user_session (trainer_id);
CREATE INDEX idx_session_expires       ON user_session (session_expires_at);