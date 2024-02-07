package com.gmail.pashkovich.al.vknewsclient.data.mapper

import com.gmail.pashkovich.al.vknewsclient.data.model.CommentsResponseDto
import com.gmail.pashkovich.al.vknewsclient.data.model.NewsFeedResponseDto
import com.gmail.pashkovich.al.vknewsclient.domain.entity.FeedPost
import com.gmail.pashkovich.al.vknewsclient.domain.entity.PostComment
import com.gmail.pashkovich.al.vknewsclient.domain.entity.StatisticItem
import com.gmail.pashkovich.al.vknewsclient.domain.entity.StatisticType
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class NewsFeedMapper @Inject constructor() {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.likes.count),
                    StatisticItem(type = StatisticType.VIEWS, post.views.count),
                    StatisticItem(type = StatisticType.SHARES, post.reposts.count),
                    StatisticItem(type = StatisticType.COMMENTS, post.comments.count)
                ),
                isLiked = post.likes.userLikes > 0
            )
            result.add(feedPost)
        }
        return result
    }

    fun mapResponseToComments(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.content.comments
        val profiles = responseDto.content.profiles

        for(comment in comments) {
            if (comment.textComment.isBlank()) continue
            val author = profiles.firstOrNull {it.id == comment.authorId} ?: continue
            val postComment = PostComment(
                id = comment.commentId,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.textComment,
                publicationDate = mapTimestampToDate(comment.dateComment)
            )
            result.add(postComment)
        }

        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}