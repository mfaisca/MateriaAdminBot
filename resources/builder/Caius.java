package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Caius extends _Default{
	public Caius() {
		super("Caius", "ballad", "ballad");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7929, 7931};
				break;
			case HP:
				ids = new int[]{7930, 7932};
				break;
			case S1:
				ids = new int[]{7156, 7155};
				break;
			case S2:
				ids = new int[]{7164, 7163};
				break;
			case EX:
				ids = new int[]{7786, 7787};
				break;
			case AA:
				ids = new int[]{7796};
				break;
			case LD:
				ids = new int[]{11119};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}