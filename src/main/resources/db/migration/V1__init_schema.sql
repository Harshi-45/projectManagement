CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),

    created_by  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    modified_by VARCHAR(255),
    modified_at TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

-- --------------------------------------------------

CREATE TABLE workspaces (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_workspace_owner
        FOREIGN KEY (owner_id) REFERENCES users(id),

    created_by  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    modified_by VARCHAR(255),
    modified_at TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE INDEX idx_workspace_owner
    ON workspaces(owner_id);

-- --------------------------------------------------

CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,

    workspace_id BIGINT NOT NULL,
    CONSTRAINT fk_project_workspace
        FOREIGN KEY (workspace_id) REFERENCES workspaces(id),

    owner_id BIGINT NOT NULL,

    created_by  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    modified_by VARCHAR(255),
    modified_at TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE INDEX idx_project_workspace
    ON projects(workspace_id);

-- --------------------------------------------------

CREATE TABLE jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    project_id BIGINT NOT NULL,
    CONSTRAINT fk_job_project
        FOREIGN KEY (project_id) REFERENCES projects(id),

    user_id BIGINT NOT NULL,
    CONSTRAINT fk_job_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    status VARCHAR(32) NOT NULL,
    retry_count BIGINT DEFAULT 0,
    idempotency_key VARCHAR(128) NOT NULL UNIQUE,
    request_payload TEXT,
    result_payload TEXT,

    created_by  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    modified_by VARCHAR(255),
    modified_at TIMESTAMP,
    deleted TINYINT(1) DEFAULT 0
);

CREATE INDEX idx_job_project
    ON jobs(project_id);

CREATE INDEX idx_job_user
    ON jobs(user_id);

-- --------------------------------------------------

CREATE TABLE workspace_members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    workspace_id BIGINT NOT NULL,
    CONSTRAINT fk_member_workspace FOREIGN KEY (workspace_id) REFERENCES workspaces(id),
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_member_user FOREIGN KEY (user_id) REFERENCES users(id),
    role VARCHAR(20) NOT NULL,
     created_by  VARCHAR(255) NOT NULL,
        created_at  TIMESTAMP NOT NULL,
        modified_by VARCHAR(255),
        modified_at TIMESTAMP,
        deleted TINYINT(1) DEFAULT 0,
    CONSTRAINT uq_workspace_user UNIQUE (workspace_id, user_id)
);

CREATE INDEX idx_member_workspace
    ON workspace_members(workspace_id);

CREATE INDEX idx_member_user
    ON workspace_members(user_id);
