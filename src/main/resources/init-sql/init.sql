create table _user
(
    id              bigserial
        constraint user_id_pk
            primary key,
    team_id         bigint,
    login           varchar(255)
        unique,
    password        varchar(250) not null,
    role            varchar(250) not null,
    name            varchar(250) not null,
    membership_type varchar(250)
);

create index team_user_id_fk
    on _user (team_id);

create table team
(
    id            bigserial
        constraint team_id_pk
            primary key,
    owner_user_id bigint
        unique
        constraint fk_owner_user_id
            references _user
            on delete cascade
);

alter table _user
    add constraint fk_user_team_id
    foreign key (team_id) references team;

create index team_owner_user_id
    on team (owner_user_id);

create table template
(
    id      bigserial
        constraint template_id_pk
            primary key,
    fields  json         not null,
    name    varchar(250) not null,
    user_id bigint       not null
        constraint fk_template_user_id
            references _user
);

create index template_user_id_fk
    on template (user_id);

create table prospect
(
    id            bigserial
        constraint prospect_id_pk
            primary key,
    template_id   bigint not null
        constraint fk_prospect_template_id
            references template,
    fields        json   not null,
    template_name varchar(250),
    user_id       bigint not null
        constraint fk_prospect_user_id
            references _user
);

create index prospect_template_id_fk
    on prospect (template_id);

create index prospect_user_id_fk
    on prospect (user_id);

