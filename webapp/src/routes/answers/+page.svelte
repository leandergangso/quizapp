<script lang="ts">
	import { goto } from "$app/navigation"
	import { quizStore } from "$lib/stores/quiz.svelte"
	import { onMount } from "svelte"

	let showAnswers = $state(false)

	function toggleAnswers() {
		showAnswers = !showAnswers
	}

	onMount(() => {
		if (!quizStore.answer) {
			goto("/", { replaceState: true })
		}
	})
</script>

{#if quizStore.answer}
	<main class="space-y-6">
		<h1>Your results</h1>
		<p>
			You scored {quizStore.answer.points} out of {quizStore.answer.totalPoints} total points.
			{#if quizStore.answer.points == quizStore.answer.totalPoints}
				<span>🎉🎉🎉</span>
			{/if}
		</p>

		<div class="flex space-x-6">
			<button onclick={() => goto("/", { replaceState: true })}>Retake quiz</button>
			<button
				onclick={toggleAnswers}
				class="bg-blue-500 text-white">Show answers</button
			>
		</div>

		{#if showAnswers}
			<ol class="flex flex-col space-y-3">
				{#each quizStore.questions as question, idx}
					<li>
						<span class="font-medium italic">Question:</span>
						<span>"{question.title}"</span>
						<br />
						<span class="font-medium italic">Answer:</span>
						{#each quizStore.answer.answers[idx] as answer}
							<span>"{answer}"{" "}</span>
						{/each}
					</li>
				{/each}
			</ol>
		{/if}
	</main>
{/if}
