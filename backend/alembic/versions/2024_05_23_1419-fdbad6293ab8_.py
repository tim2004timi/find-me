"""empty message

Revision ID: fdbad6293ab8
Revises: 29f8a7440317
Create Date: 2024-05-23 14:19:29.101312

"""

from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = "fdbad6293ab8"
down_revision: Union[str, None] = "29f8a7440317"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table(
        "chats",
        sa.Column("first_user_id", sa.Integer(), nullable=False),
        sa.Column("second_user_id", sa.Integer(), nullable=False),
        sa.Column("first_user_messages_amount", sa.Integer(), nullable=False),
        sa.Column("second_user_messages_amount", sa.Integer(), nullable=False),
        sa.Column("first_user_adequacy_sum", sa.Float(), nullable=False),
        sa.Column("second_user_adequacy_sum", sa.Float(), nullable=False),
        sa.Column("id", sa.Integer(), nullable=False),
        sa.ForeignKeyConstraint(
            ["first_user_id"],
            ["users.id"],
        ),
        sa.ForeignKeyConstraint(
            ["second_user_id"],
            ["users.id"],
        ),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(op.f("ix_chats_id"), "chats", ["id"], unique=False)
    op.create_table(
        "messages",
        sa.Column("chat_id", sa.Integer(), nullable=False),
        sa.Column("from_user_id", sa.Integer(), nullable=False),
        sa.Column("text", sa.String(), nullable=False),
        sa.Column("adequacy", sa.Float(), nullable=False),
        sa.Column("created_at", sa.DateTime(), nullable=False),
        sa.Column("id", sa.Integer(), nullable=False),
        sa.ForeignKeyConstraint(
            ["chat_id"],
            ["chats.id"],
        ),
        sa.ForeignKeyConstraint(
            ["from_user_id"],
            ["users.id"],
        ),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(op.f("ix_messages_id"), "messages", ["id"], unique=False)
    # ### end Alembic commands ###


def downgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_index(op.f("ix_messages_id"), table_name="messages")
    op.drop_table("messages")
    op.drop_index(op.f("ix_chats_id"), table_name="chats")
    op.drop_table("chats")
    # ### end Alembic commands ###